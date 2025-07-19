package ma.emsi.backendevents.controller;

import ma.emsi.backendevents.entity.User;
import ma.emsi.backendevents.enums.UserRole;
import ma.emsi.backendevents.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/test")
public class TestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TestController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/db-status")
    public ResponseEntity<Map<String, Object>> checkDatabaseStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Try to count users to verify database connection
            long userCount = userRepository.count();
            response.put("status", "success");
            response.put("message", "Database connection successful");
            response.put("userCount", userCount);
            
            // Check if test user exists
            Optional<User> testUser = userRepository.findByEmail("test@test.com");
            response.put("testUserExists", testUser.isPresent());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Database connection failed");
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @PostMapping("/create-test-user")
    public ResponseEntity<Map<String, Object>> createTestUser() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if test user already exists
            Optional<User> existingUser = userRepository.findByEmail("test@test.com");
            
            if (existingUser.isPresent()) {
                response.put("status", "info");
                response.put("message", "Test user already exists");
                return ResponseEntity.ok(response);
            }
            
            // Create a test user with a known password
            User testUser = User.builder()
                    .fullName("Test User")
                    .email("test@test.com")
                    .password(passwordEncoder.encode("test123"))
                    .role(UserRole.USER)
                    .build();
            
            userRepository.save(testUser);
            
            response.put("status", "success");
            response.put("message", "Test user created successfully");
            response.put("credentials", Map.of(
                "email", "test@test.com",
                "password", "test123"
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to create test user");
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
