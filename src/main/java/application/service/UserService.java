package application.service;

import application.db.UserDatabase;
import application.model.User;

public class UserService {
    private final UserDatabase userDatabase = new UserDatabase();

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
}
