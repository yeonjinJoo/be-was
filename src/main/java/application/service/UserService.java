package application.service;

import application.repository.UserRepository;
import application.model.User;
import webserver.exception.BadRequestException;
import webserver.exception.ConflictException;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user) {
        validDuplicateUser(user.getUserId());
        userRepository.addUser(user);
    }

    private void validDuplicateUser(String userId) {
        userRepository.findUserById(userId)
                .ifPresent(user -> {
                    throw ConflictException.dupliacteUserId();
                });
    }

    public User login(String userId, String password) {
        User user = userRepository.login(userId, password)
                .orElseThrow(() -> BadRequestException.invalidLogin());

        return user;
    }
}
