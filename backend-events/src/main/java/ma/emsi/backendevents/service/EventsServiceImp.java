package ma.emsi.backendevents.service;

import ma.emsi.backendevents.entity.Events;
import ma.emsi.backendevents.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventsServiceImp implements EventsService{

    @Autowired
    private EventsRepository eventsRepository;

    @Override
    public List<Events> getAllEvents() {
        return eventsRepository.findAllByOrderByEventDateDesc();
    }

    @Override
    public Events getEventById(Long eventId) {
        Optional<Events> optionalEvent = eventsRepository.findById(eventId);
        return optionalEvent.orElse(null);
    }

    @Override
    public Events createEvent(Events events) {
        return eventsRepository.save(events);
    }

    @Override
    public Events updateEvent(Long eventId, Events events) {
        if(eventsRepository.existsById(eventId)){
            events.setEnventId(eventId);
            return eventsRepository.save(events);
        }
        return null;
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventsRepository.deleteById(eventId);
    }

    @Override
    public List<Events> searchEvents(String query) {
        return eventsRepository.findByEventNameContainingIgnoreCaseOrEventPlaceContainingIgnoreCaseOrderByEventDateDesc(query, query);
    }

    @Override
    public void deleteMultipleEvents(List<Long> eventIds) {
        eventsRepository.deleteByEnventIdIn(eventIds);
    }
}
