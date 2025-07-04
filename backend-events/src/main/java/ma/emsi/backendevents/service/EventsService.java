package ma.emsi.backendevents.service;

import ma.emsi.backendevents.entity.Events;

import java.util.List;

public interface EventsService {

    List<Events> getAllEvents();

    Events getEventById(Long eventId);

    Events createEvent(Events events);

    Events updateEvent(Long eventId,Events events);

    void deleteEvent(Long eventId);

    List<Events> searchEvents(String query);

    void deleteMultipleEvents(List<Long> eventIds);
}
