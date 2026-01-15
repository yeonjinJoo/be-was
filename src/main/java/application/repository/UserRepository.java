package application.repository;

import application.model.User;

import java.sql.Connection;
import java.util.Optional;

public interface UserRepository {
    void addUser(Connection conn, User user);

    Optional<User> findByUserId(Connection conn, String userId);

    User findById(Connection conn, int id);

    boolean existsByUserId(Connection conn, String userId);

    boolean existsByUserName(Connection conn, String userName);

    void updateProfile(Connection conn, int id, String newName, String newPw);
}
