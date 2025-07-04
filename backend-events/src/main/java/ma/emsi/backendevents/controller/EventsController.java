package ma.emsi.backendevents.controller;
import ma.emsi.backendevents.entity.Events;
import ma.emsi.backendevents.exceptions.EventNotFoundException;
import ma.emsi.backendevents.service.EventsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:4200")
public class EventsController {

    @Autowired private EventsServiceImp eventsServiceImp;

    @GetMapping
    public ResponseEntity<List<Events>> getAllEvents(){
        List<Events> events = eventsServiceImp.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Events> getEventById(@PathVariable Long eventId) {
        try {
            Events event = eventsServiceImp.getEventById(eventId);
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (EventNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Events> createEvent(@RequestBody Events event){
        Events createEvent = eventsServiceImp.createEvent(event);
        return new ResponseEntity<>(createEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Events> updateEvent(@PathVariable Long eventId, @RequestBody Events event){
        Events updateEvent = eventsServiceImp.updateEvent(eventId,event);
        return new ResponseEntity<>(updateEvent, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId){
        eventsServiceImp.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Events>> searchEvents(@RequestParam("query") String query) {
        List<Events> events = eventsServiceImp.searchEvents(query);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("/batch-delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteMultipleEvents(@RequestBody List<Long> eventIds) {
        eventsServiceImp.deleteMultipleEvents(eventIds);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
