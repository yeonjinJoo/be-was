package application.service;

import db.UserDatabase;
import application.model.User;
import webserver.exception.BadRequestException;
import webserver.exception.ConflictException;

import java.util.Optional;

public class UserService {
    private final UserDatabase userDatabase;

    public UserService(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    public void create(User user) {
        validDuplicateUser(user.getUserId());
        userDatabase.addUser(user);
    }

    private void validDuplicateUser(String userId) {
        userDatabase.findUserById(userId)
                .ifPresent(user -> {
                    throw ConflictException.dupliacteUserId();
                });
    }

    public User login(String userId, String password) {
        User user = userDatabase.findUserById(userId)
                .orElseThrow(() -> BadRequestException.invalidLogin());

        if(!user.getPassword().equals(password)){
            throw BadRequestException.invalidLogin();
        }

        return user;
    }
}
