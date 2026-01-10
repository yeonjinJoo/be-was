package application.service;

import db.UserDatabase;
import application.model.User;

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
                .ifPresent(message -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다.");
                });
    }

    public User login(String userId, String password) {
        Optional<User> user = userDatabase.findUserById(userId);

        if(user.isEmpty() || !user.get().getPassword().equals(password)) {
            return null;
        }

        return user.get();
    }
}
