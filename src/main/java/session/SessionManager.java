package session;

import application.model.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Map<String, User> sessions = new ConcurrentHashMap<>();

    public String createSession(User user) {
        String sid = UUID.randomUUID().toString();
        sessions.put(sid, user);
        return sid;
    }

    public User getUser(String sid) {
        if (sid == null) return null;
        return sessions.get(sid);
    }

    public void invalidate(String sid) {
        if (sid == null) return;
        sessions.remove(sid);
    }

}
