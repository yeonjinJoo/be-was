package application.repository;

import application.model.User;
import java.util.Optional;

public interface UserRepository {
    public void addUser(User user);

    public Optional<User> login(String userId,  String password);

    public Optional<User> findUserById(String userId);
}
