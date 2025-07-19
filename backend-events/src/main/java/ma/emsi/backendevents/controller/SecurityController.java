package ma.emsi.backendevents.controller;

import ma.emsi.backendevents.DTO.RegisterRequest;
import ma.emsi.backendevents.entity.User;
import ma.emsi.backendevents.enums.UserRole;
import ma.emsi.backendevents.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public Authentication authentication(Authentication authentication){
        return authentication;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        System.out.println("Login attempt received: " + loginRequest);
        
        try {
            // Extract credentials
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            
            System.out.println("Attempting authentication for email: " + email);
            System.out.println("Password length: " + (password != null ? password.length() : "null"));
            
            // Try authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            
            System.out.println("Authentication successful for: " + email);
            System.out.println("Authorities: " + authentication.getAuthorities());

            // Generate JWT token
            Instant instant = Instant.now();
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(10, ChronoUnit.DAYS))
                    .subject(email)
                    .claim("scope", scope)
                    .build();
            
            System.out.println("JWT claims created: " + jwtClaimsSet.getClaims());

            // Encode the token
            String jwt = jwtEncoder.encode(JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),
                    jwtClaimsSet
            )).getTokenValue();
            
            System.out.println("JWT token generated, length: " + jwt.length());

            // Return the token
            Map<String, String> response = new HashMap<>();
            response.put("access-token", jwt);
            System.out.println("Login successful, returning token");
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Unexpected error during login: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest registerRequest) {
        Map<String, String> response = new HashMap<>();
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            response.put("status", "error");
            response.put("message", "Email is already taken!");
            return ResponseEntity.badRequest().body(response);
        }

        User user = User.builder()
                .fullName(registerRequest.getFullName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        response.put("status", "success");
        response.put("message", "User registered successfully!");
        return ResponseEntity.ok(response);
    }
}
