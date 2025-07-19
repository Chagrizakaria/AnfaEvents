package ma.emsi.backendevents.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller to handle requests to the root path of the API
 */
@RestController
@CrossOrigin(origins = "*") // Allow requests from any origin
public class RootController {

    /**
     * Endpoint for the root path of the API
     * This provides a public welcome page that doesn't require authentication
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "online");
        response.put("message", "Welcome to AnfaEvents API");
        response.put("documentation", "/api/public/status");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }
}
