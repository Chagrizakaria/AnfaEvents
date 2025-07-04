package ma.emsi.backendevents.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import ma.emsi.backendevents.enums.EventCategory;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
public class Events {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enventId;
    @NotEmpty
    private String eventName;
    @Enumerated(EnumType.STRING)
    private EventCategory eventCategory;
    private String eventCity;
    private String eventPlace;
    @Temporal(TemporalType.DATE)
    private Date eventDate;
    private double eventPrice;
    @Lob
    @Column(length = 50000)
    private String eventDetails;
    @Lob
    @Column(length = 50000)
    private String imgUrl;

    public Events() {
    }

    public Events(Long enventId, String eventName, EventCategory eventCategory, String eventCity, String eventPlace, Date eventDate, double eventPrice, String eventDetails, String imgUrl) {
        this.enventId = enventId;
        this.eventName = eventName;
        this.eventCategory = eventCategory;
        this.eventCity = eventCity;
        this.eventPlace = eventPlace;
        this.eventDate = eventDate;
        this.eventPrice = eventPrice;
        this.eventDetails = eventDetails;
        this.imgUrl = imgUrl;
    }

    // Getters and Setters
    public Long getEnventId() {
        return enventId;
    }

    public void setEnventId(Long enventId) {
        this.enventId = enventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventCity() {
        return eventCity;
    }

    public void setEventCity(String eventCity) {
        this.eventCity = eventCity;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    // Builder pattern
    public static EventsBuilder builder() {
        return new EventsBuilder();
    }

    public static class EventsBuilder {
        private Long enventId;
        private String eventName;
        private EventCategory eventCategory;
        private String eventCity;
        private String eventPlace;
        private Date eventDate;
        private double eventPrice;
        private String eventDetails;
        private String imgUrl;

        EventsBuilder() {
        }

        public EventsBuilder enventId(Long enventId) {
            this.enventId = enventId;
            return this;
        }

        public EventsBuilder eventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public EventsBuilder eventCategory(EventCategory eventCategory) {
            this.eventCategory = eventCategory;
            return this;
        }

        public EventsBuilder eventCity(String eventCity) {
            this.eventCity = eventCity;
            return this;
        }

        public EventsBuilder eventPlace(String eventPlace) {
            this.eventPlace = eventPlace;
            return this;
        }

        public EventsBuilder eventDate(Date eventDate) {
            this.eventDate = eventDate;
            return this;
        }

        public EventsBuilder eventPrice(double eventPrice) {
            this.eventPrice = eventPrice;
            return this;
        }

        public EventsBuilder eventDetails(String eventDetails) {
            this.eventDetails = eventDetails;
            return this;
        }

        public EventsBuilder imgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public Events build() {
            return new Events(enventId, eventName, eventCategory, eventCity, eventPlace, eventDate, eventPrice, eventDetails, imgUrl);
        }
    }
}
