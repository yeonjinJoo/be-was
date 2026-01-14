package application.service;

import application.repository.UserRepository;
import application.model.User;
import webserver.exception.webexception.BadRequestException;
import webserver.exception.webexception.ConflictException;

public class UserService {
    private static final String LOGIN_PAGE = "/login/index.html";
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user) {
        if(isDuplicateUser(user.getUserId())) throw ConflictException.duplicateUserId(LOGIN_PAGE);
        if(isDuplicateName(user.getName())) throw ConflictException.duplicateUserName(LOGIN_PAGE);
        userRepository.addUser(user);
    }

    public boolean isDuplicateUser(String userId) {
        return userRepository.existsByUserId(userId) ? true : false;
    }

    public boolean isDuplicateName(String name){
        return userRepository.existsByUserName(name) ? true : false;
    }

    public User login(String userId, String password) {
        User user = userRepository.login(userId)
                .orElseThrow(() -> BadRequestException.invalidUserId());

        if(!user.getPassword().equals(password)) {
            throw BadRequestException.invalidPassword();
        }

        return user;
    }

    public boolean isValidLength(String... values){
        for(String value : values){
            if(value.length() < 4){
                return false;
            }
        }
        return true;
    }
}
