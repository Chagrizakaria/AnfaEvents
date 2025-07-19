package ma.emsi.backendevents.controller;

import ma.emsi.backendevents.entity.Events;
import ma.emsi.backendevents.service.EventsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
public class PublicController {

    @Autowired
    private EventsServiceImp eventsServiceImp;

    @GetMapping("/events")
    public ResponseEntity<List<Events>> getAllEvents(){
        List<Events> events = eventsServiceImp.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getApiStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "online");
        status.put("message", "AnfaEvents API is running");
        status.put("version", "1.0.0");
        return ResponseEntity.ok(status);
    }
}
