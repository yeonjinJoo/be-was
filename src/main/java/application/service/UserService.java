package application.service;

import application.repository.JdbcTx;
import application.repository.UserRepository;
import application.model.User;
import webserver.exception.webexception.BadRequestException;
import webserver.exception.webexception.ConflictException;

import java.util.Optional;

public class UserService {
    private static final String LOGIN_PAGE = "/login/index.html";
    private static final String REGISTRATION_PAGE = "/registration/index.html";
    private static final String MY_PAGE = "/mypage";
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user) {
        validateLength(REGISTRATION_PAGE, user.getUserId(), user.getName(), user.getEmail(), user.getPassword());

        JdbcTx.executeInTx(conn -> {
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

    public Optional<User> changeProfile(int id, String newName, String newPassword, String confirmPassword) {
        String newNm = normalize(newName);
        String newPw =  normalize(newPassword);
        String confirmPw = normalize(confirmPassword);

        if (newNm == null && newPw == null && confirmPw == null) {
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

        return Optional.ofNullable(JdbcTx.executeInTx(conn -> {
            if (newNm != null && userRepository.existsByUserName(conn, newNm)) {
                throw ConflictException.duplicateUserName(MY_PAGE);
            }

            userRepository.updateProfile(conn, id, newNm, newPw);
            return userRepository.findById(conn, id);
        }));
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

}
