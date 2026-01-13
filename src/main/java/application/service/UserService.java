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
        validDuplicateName(user.getName());
        userRepository.addUser(user);
    }

    private void validDuplicateUser(String userId) {
        if(userRepository.existsByUserId(userId)) {
            throw ConflictException.duplicateUserId();
        }
    }

    private void validDuplicateName(String name){
        if(userRepository.existsByUserName(name)){
            throw ConflictException.dupliacteUserName();
        }
    }

    public User login(String userId, String password) {
        User user = userRepository.login(userId)
                .orElseThrow(() -> BadRequestException.invalidUserId());

        if(!user.getPassword().equals(password)) {
            throw BadRequestException.invalidPassword();
        }

        return user;
    }
}
