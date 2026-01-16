package application.service;

import application.ImageStorage;
import application.repository.JdbcTx;
import application.repository.UserRepository;
import application.model.User;
import webserver.exception.webexception.BadRequestException;
import webserver.exception.webexception.ConflictException;
import webserver.multipart.UploadedFile;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class UserService {
    private static final String LOGIN_PAGE = "/login";
    private static final String REGISTRATION_PAGE = "/registration";
    private static final String MY_PAGE = "/mypage";
    private final UserRepository userRepository;
    private final ImageStorage imageStorage;

    public UserService(UserRepository userRepository, ImageStorage imageStorage) {
        this.userRepository = userRepository;
        this.imageStorage = imageStorage;
    }

    public void create(User user) {
        validateLength(REGISTRATION_PAGE, user.getUserId(), user.getName(), user.getEmail(), user.getPassword());

        JdbcTx.execute(conn -> {
            if (userRepository.existsByUserId(conn, user.getUserId())) {
                throw ConflictException.duplicateUserId(REGISTRATION_PAGE);
            }
            if (userRepository.existsByUserName(conn, user.getName())) {
                throw ConflictException.duplicateUserName(REGISTRATION_PAGE);
            }
            userRepository.addUser(conn, user);
            return null;
        });
    }

    public User login(String userId, String password) {
        validateLength(LOGIN_PAGE, userId, password);
        return JdbcTx.execute(conn -> {
            User user = userRepository.findByUserId(conn, userId)
                    .orElseThrow(() -> BadRequestException.invalidUserId());

            if (!user.getPassword().equals(password)) {
                throw BadRequestException.invalidPassword();
            }
            return user;
        });
    }

    public Optional<User> changeProfile(int id,
                                        String newName,
                                        String newPassword,
                                        String confirmPassword,
                                        UploadedFile image,
                                        boolean deleteProfile
    ) {
        String newNm = normalize(newName);
        String newPw =  normalize(newPassword);
        String confirmPw = normalize(confirmPassword);

        boolean hasTextChange = (newNm != null || newPw != null || confirmPw != null);
        boolean hasImageChange = (image != null || deleteProfile);

        if (!hasTextChange && !hasImageChange) {
            return Optional.empty();
        }

        if (newNm != null) {
            validateLength(MY_PAGE, newNm);
        }

        if((newPw != null && confirmPw == null) || (newPw == null && confirmPw != null)) {
            throw BadRequestException.missingChangePassword();
        }

        if(newPw != null && confirmPw != null) {
            if (!newPw.equals(confirmPw)) throw BadRequestException.invalidChangePassword();
            validateLength(MY_PAGE, newPw);
        }

        String savedFileName = null;
        AtomicReference<String> oldImageUrl = new AtomicReference<>();
        try{
            if (image != null) {
                savedFileName = imageStorage.save(image);
            }

            String finalSavedFileName = savedFileName;

            User updated = JdbcTx.executeInTx(conn -> {
                if (newNm != null && userRepository.existsByUserName(conn, newNm)) {
                    throw ConflictException.duplicateUserName(MY_PAGE);
                }

                User user = userRepository.findById(conn, id);
                oldImageUrl.set(user.getProfileImageUrl());

                String newImageUrl = oldImageUrl.get();

                if (deleteProfile) {
                    newImageUrl = null;
                }
                if (finalSavedFileName != null) {
                    newImageUrl = "/img/uploads/" + finalSavedFileName;
                }

                userRepository.updateProfile(conn, id, newNm, newPw, hasImageChange, newImageUrl);

                return userRepository.findById(conn, id);
            });

            // 성공했다면 기존 이미지 삭제
            if ((deleteProfile || finalSavedFileName != null) && oldImageUrl.get() != null) {
                imageStorage.deleteQuietly(extractFileName(oldImageUrl.get()));
            }

            return Optional.ofNullable(updated);

        } catch (RuntimeException e) {
            // DB 실패(또는 중간 실패) → 새로 저장한 파일만 롤백
            if (savedFileName != null) {
                imageStorage.deleteQuietly(savedFileName);
            }
            throw e;
        }
    }

    private void validateLength(String redirectPath, String... values){
        for(String value : values){
            if(value.length() < 4){
                throw BadRequestException.invalidParameters(redirectPath);
            }
        }
    }

    private String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private String extractFileName(String imageUrl) {
        if (imageUrl == null) return null;
        int idx = imageUrl.lastIndexOf('/');
        return (idx >= 0) ? imageUrl.substring(idx + 1) : imageUrl;
    }


}
