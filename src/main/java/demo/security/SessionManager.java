package demo.security;

import demo.model.Role;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    
    public static class Session {
        private final String token;
        private final long userId;
        private final Role role;
        private final Instant createdAt;

        public Session(String token, long userId, Role role) {
            this.token = token;
            this.userId = userId;
            this.role = role;
            this.createdAt = Instant.now();
        }

        public String getToken() { return token; }
        public long getUserId() { return userId; }
        public Role getRole() { return role; }
        public Instant getCreatedAt() { return createdAt; }
    }

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public void createSession(String token, long userId, Role role) {
        sessions.put(token, new Session(token, userId, role));
    }

    public Optional<Session> getSession(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(sessions.get(token));
    }

    public void invalidateSession(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }
}
