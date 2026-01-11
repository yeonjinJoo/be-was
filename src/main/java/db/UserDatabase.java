package db;

import application.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserDatabase {
    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public Optional<User> findUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
