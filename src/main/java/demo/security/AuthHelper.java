package demo.security;

import demo.model.Role;
import demo.security.SessionManager.Session;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthHelper {

    private final SessionManager sessionManager;

    public AuthHelper(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Session validateAndGetSession(String token) {
        return sessionManager.getSession(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing authentication token"));
    }

    public void verifyAdminRole(Session session) {
        if (session.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied: Requires Administrator privileges");
        }
    }
}
