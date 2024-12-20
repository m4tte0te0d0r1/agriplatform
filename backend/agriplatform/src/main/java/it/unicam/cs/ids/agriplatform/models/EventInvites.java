package it.unicam.cs.ids.agriplatform.models;

import jakarta.persistence.*;

@Entity
@Table(name = "event_invites")
public class EventInvites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @GeneratedValue
    private long eventId;

    @Column(nullable = false)
    @GeneratedValue
    private long userId;

    public EventInvites(long id, long eventId, long userId) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }   

    public void setId(long id) {
        this.id = id;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}