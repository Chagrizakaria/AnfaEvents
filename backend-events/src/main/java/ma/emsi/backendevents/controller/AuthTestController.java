package ma.emsi.backendevents.controller;

import ma.emsi.backendevents.entity.User;
import ma.emsi.backendevents.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Test controller for authentication debugging
 */
@RestController
@RequestMapping("/api/public/auth-test")
public class AuthTestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthTestController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Simple endpoint to check if the controller is accessible
     */
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "AuthTestController is working");
        return ResponseEntity.ok(response);
    }

    /**
     * Check if a user exists in the database
     */
    @GetMapping("/check-user/{email}")
    public ResponseEntity<Map<String, Object>> checkUser(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            response.put("status", "success");
            response.put("exists", true);
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "fullName", user.getFullName(),
                "role", user.getRole().toString(),
                "passwordLength", user.getPassword().length()
            ));
        } else {
            response.put("status", "success");
            response.put("exists", false);
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * Test password matching for a user
     */
    @PostMapping("/check-password")
    public ResponseEntity<Map<String, Object>> checkPassword(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        String email = request.get("email");
        String password = request.get("password");
        
        if (email == null || password == null) {
            response.put("status", "error");
            response.put("message", "Email and password are required");
            return ResponseEntity.badRequest().body(response);
        }
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            response.put("status", "error");
            response.put("message", "User not found");
            return ResponseEntity.ok(response);
        }
        
        User user = userOpt.get();
        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        
        response.put("status", "success");
        response.put("passwordMatches", passwordMatches);
        response.put("encodedPasswordLength", user.getPassword().length());
        response.put("providedPasswordLength", password.length());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Echo back the request to check what the server is receiving
     */
    @PostMapping("/echo")
    public ResponseEntity<Map<String, Object>> echo(@RequestBody(required = false) Map<String, Object> request,
                                                   @RequestHeader Map<String, String> headers) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("receivedHeaders", headers);
        response.put("receivedBody", request != null ? request : "No body or not JSON");
        return ResponseEntity.ok(response);
    }
}
