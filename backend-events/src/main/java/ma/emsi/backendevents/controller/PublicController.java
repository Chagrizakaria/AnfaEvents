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

import java.util.List;

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
}
