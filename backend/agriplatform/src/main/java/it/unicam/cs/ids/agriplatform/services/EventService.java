package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.input.event.CreateEventDTO;
import it.unicam.cs.ids.agriplatform.dto.input.event.UpdateEventDTO;
import it.unicam.cs.ids.agriplatform.dto.output.EventResponseDTO;
import it.unicam.cs.ids.agriplatform.dto.output.EventInviteResponseDTO;
import it.unicam.cs.ids.agriplatform.models.Event;
import it.unicam.cs.ids.agriplatform.models.EventInvites;
import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.EventRepository;
import it.unicam.cs.ids.agriplatform.repositories.EventInvitesRepository;
import it.unicam.cs.ids.agriplatform.utils.UserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventInvitesRepository eventInvitesRepository;
    private final UserContext userContext;

    @Autowired
    public EventService(EventRepository eventRepository, EventInvitesRepository eventInvitesRepository,
            UserContext userContext) {
        this.eventRepository = eventRepository;
        this.eventInvitesRepository = eventInvitesRepository;
        this.userContext = userContext;
    }

    /**
     * Get all events ordered by date
     */
    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAllByOrderByEventDateAsc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming events (events after current date)
     */
    public List<EventResponseDTO> getUpcomingEvents() {
        return eventRepository.findByEventDateAfterOrderByEventDateAsc(LocalDateTime.now())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get past events
     */
    public List<EventResponseDTO> getPastEvents() {
        return eventRepository.findByEventDateBeforeOrderByEventDateDesc(LocalDateTime.now())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get an event by its ID
     */
    public Optional<EventResponseDTO> getEventById(Long id) {
        return eventRepository.findById(id)
                .map(this::mapToDTO);
    }

    /**
     * Search events by name
     */
    public List<EventResponseDTO> searchEventsByName(String name) {
        return eventRepository.findByNameContainingIgnoreCaseOrderByEventDateAsc(name)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search events by location
     */
    public List<EventResponseDTO> searchEventsByLocation(String location) {
        return eventRepository.findByLocationContainingIgnoreCaseOrderByEventDateAsc(location)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new event
     */
    public EventResponseDTO createEvent(CreateEventDTO eventDTO) {
        Event event = new Event(
                0L, // ID will be generated
                eventDTO.name(),
                eventDTO.description(),
                eventDTO.eventDate(),
                eventDTO.location());

        Event savedEvent = eventRepository.save(event);
        return mapToDTO(savedEvent);
    }

    /**
     * Update an existing event
     */
    public Optional<EventResponseDTO> updateEvent(Long id, UpdateEventDTO eventDTO) {
        return eventRepository.findById(id).map(existingEvent -> {
            existingEvent.setName(eventDTO.name());
            existingEvent.setDescription(eventDTO.description());
            existingEvent.setEventDate(eventDTO.eventDate());
            existingEvent.setLocation(eventDTO.location());

            Event updatedEvent = eventRepository.save(existingEvent);
            return mapToDTO(updatedEvent);
        });
    }

    /**
     * Delete an event by ID
     */
    @Transactional
    public boolean deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            // Delete all invites for this event first
            eventInvitesRepository.deleteByEventId(id);
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ==================== EVENT INVITES METHODS ====================

    /**
     * Invite a user to an event
     */
    public EventInviteResponseDTO inviteUserToEvent(Long eventId, Long userId) {
        // Check if event exists
        if (!eventRepository.existsById(eventId)) {
            throw new IllegalArgumentException("Event not found with id: " + eventId);
        }

        // Check if invite already exists
        if (eventInvitesRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new IllegalStateException("User is already invited to this event");
        }

        EventInvites invite = new EventInvites(0L, eventId, userId);
        EventInvites savedInvite = eventInvitesRepository.save(invite);
        return mapInviteToDTO(savedInvite);
    }

    /**
     * Get all invites for an event
     */
    public List<EventInviteResponseDTO> getEventInvites(Long eventId) {
        return eventInvitesRepository.findByEventId(eventId)
                .stream()
                .map(this::mapInviteToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all events a user is invited to
     */
    public List<EventResponseDTO> getUserInvitedEvents(Long userId) {
        List<EventInvites> invites = eventInvitesRepository.findByUserId(userId);
        return invites.stream()
                .map(invite -> eventRepository.findById(invite.getEventId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get events the current user is invited to
     */
    public List<EventResponseDTO> getCurrentUserInvitedEvents() {
        User currentUser = userContext.getCurrentUser();
        return getUserInvitedEvents(currentUser.getId());
    }

    /**
     * Remove an invite
     */
    @Transactional
    public boolean removeInvite(Long eventId, Long userId) {
        if (eventInvitesRepository.existsByEventIdAndUserId(eventId, userId)) {
            eventInvitesRepository.deleteByEventIdAndUserId(eventId, userId);
            return true;
        }
        return false;
    }

    /**
     * Check if a user is invited to an event
     */
    public boolean isUserInvited(Long eventId, Long userId) {
        return eventInvitesRepository.existsByEventIdAndUserId(eventId, userId);
    }

    // ==================== MAPPING METHODS ====================

    /**
     * Map Event entity to EventResponseDTO
     */
    private EventResponseDTO mapToDTO(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getEventDate(),
                event.getLocation());
    }

    /**
     * Map EventInvites entity to EventInviteResponseDTO
     */
    private EventInviteResponseDTO mapInviteToDTO(EventInvites invite) {
        return new EventInviteResponseDTO(
                invite.getId(),
                invite.getEventId(),
                invite.getUserId());
    }
}
