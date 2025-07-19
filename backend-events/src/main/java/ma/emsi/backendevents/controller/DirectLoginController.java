package ma.emsi.backendevents.controller;

import ma.emsi.backendevents.entity.User;
import ma.emsi.backendevents.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A controller that provides direct login functionality bypassing Spring Security
 * for debugging purposes only.
 */
@RestController
@RequestMapping("/api/public/direct-login")
public class DirectLoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    public DirectLoginController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Direct login endpoint that bypasses Spring Security
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> directLogin(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("Direct login attempt received: " + loginRequest);
            
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            
            if (email == null || password == null) {
                response.put("status", "error");
                response.put("message", "Email and password are required");
                return ResponseEntity.badRequest().body(response);
            }
            
            Optional<User> userOpt = userRepository.findByEmail(email);
            
            if (userOpt.isEmpty()) {
                System.out.println("User not found: " + email);
                response.put("status", "error");
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
            
            System.out.println("Password match result: " + passwordMatches);
            
            if (!passwordMatches) {
                response.put("status", "error");
                response.put("message", "Password incorrect");
                return ResponseEntity.ok(response);
            }
            
            // Generate a simple JWT token manually
            String token = generateSimpleJwt(email, user.getRole().toString());
            
            response.put("status", "success");
            response.put("message", "Login successful");
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "fullName", user.getFullName(),
                "role", user.getRole().toString()
            ));
            response.put("access-token", token);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error in direct login: " + e.getMessage());
            e.printStackTrace();
            
            response.put("status", "error");
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Generate a simple JWT token manually without using Spring Security
     */
    private String generateSimpleJwt(String subject, String role) {
        try {
            // Create a simple header
            String header = Base64.getUrlEncoder().encodeToString(
                "{\"alg\":\"HS512\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8)
            );
            
            // Create payload with claims
            Instant now = Instant.now();
            String payload = Base64.getUrlEncoder().encodeToString(
                String.format(
                    "{\"sub\":\"%s\",\"scope\":\"%s\",\"iat\":%d,\"exp\":%d}", 
                    subject, 
                    role, 
                    now.getEpochSecond(),
                    now.plus(10, ChronoUnit.DAYS).getEpochSecond()
                ).getBytes(StandardCharsets.UTF_8)
            );
            
            // Create signature
            String data = header + "." + payload;
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8), 
                "HmacSHA512"
            );
            mac.init(secretKeySpec);
            String signature = Base64.getUrlEncoder().encodeToString(
                mac.doFinal(data.getBytes(StandardCharsets.UTF_8))
            );
            
            // Combine all parts
            return header + "." + payload + "." + signature;
            
        } catch (Exception e) {
            System.err.println("Error generating JWT: " + e.getMessage());
            e.printStackTrace();
            return "ERROR-GENERATING-TOKEN";
        }
    }
}
