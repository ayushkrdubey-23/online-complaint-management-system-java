package demo.service;

import demo.dto.LoginRequest;
import demo.dto.LoginResponse;
import demo.dto.RegisterRequest;
import demo.model.Role;
import demo.model.User;
import demo.repository.UserRepository;
import demo.security.SessionManager;
import demo.utility.IdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;
    private final IdGenerator idGenerator;
    private static final SecureRandom RNG = new SecureRandom();

    public UserService(UserRepository userRepository, SessionManager sessionManager, IdGenerator idGenerator) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
        this.idGenerator = idGenerator;
    }

    public void registerUser(RegisterRequest req) {
        if (req.getName() == null || req.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be blank");
        }
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be blank");
        }
        if (req.getPassword() == null || req.getPassword().length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 4 characters long");
        }
        
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        long id = idGenerator.nextUserId();
        User user = new User(id, req.getName(), req.getEmail(), req.getPassword(), Role.USER);
        userRepository.save(user);
    }

    public void createInitialUser(String name, String email, String password, Role role) {
        long id = idGenerator.nextUserId();
        User user = new User(id, name, email, password, role);
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest req) {
        if (req.getEmail() == null || req.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        User user = userRepository.findByEmail(req.getEmail())
                .filter(u -> u.getPassword().equals(req.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        String token = generateNewToken();
        sessionManager.createSession(token, user.getId(), user.getRole());
        
        return new LoginResponse(token, user.getId(), user.getRole());
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private String generateNewToken() {
        byte[] bytes = new byte[24];
        RNG.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
