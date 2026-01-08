package application.service;

import application.db.UserDatabase;
import application.model.User;

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

        User user = userDatabase.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if(!user.getPassword().equals(password)) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}
