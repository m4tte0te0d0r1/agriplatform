package it.unicam.cs.ids.agriplatform.controllers.event;

import it.unicam.cs.ids.agriplatform.dto.input.event.CreateEventDTO;
import it.unicam.cs.ids.agriplatform.dto.input.event.UpdateEventDTO;
import it.unicam.cs.ids.agriplatform.dto.output.EventResponseDTO;
import it.unicam.cs.ids.agriplatform.dto.output.EventInviteResponseDTO;
import it.unicam.cs.ids.agriplatform.services.EventService;
import it.unicam.cs.ids.agriplatform.utils.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // ==================== EVENT ENDPOINTS ====================

    /**
     * GET /api/events - Get all events
     */
    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ApiResponse.ok("Events retrieved successfully", events);
    }

    /**
     * GET /api/events/upcoming - Get upcoming events
     */
    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingEvents() {
        List<EventResponseDTO> events = eventService.getUpcomingEvents();
        return ApiResponse.ok("Upcoming events retrieved successfully", events);
    }

    /**
     * GET /api/events/past - Get past events
     */
    @GetMapping("/past")
    public ResponseEntity<?> getPastEvents() {
        List<EventResponseDTO> events = eventService.getPastEvents();
        return ApiResponse.ok("Past events retrieved successfully", events);
    }

    /**
     * GET /api/events/{id} - Get a specific event by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(event -> ApiResponse.ok("Event retrieved successfully", event))
                .orElse(ApiResponse.notFound("Event not found with id: " + id));
    }

    /**
     * GET /api/events/search/name?q={name} - Search events by name
     */
    @GetMapping("/search/name")
    public ResponseEntity<?> searchEventsByName(@RequestParam String q) {
        List<EventResponseDTO> events = eventService.searchEventsByName(q);
        return ApiResponse.ok("Events found", events);
    }

    /**
     * GET /api/events/search/location?q={location} - Search events by location
     */
    @GetMapping("/search/location")
    public ResponseEntity<?> searchEventsByLocation(@RequestParam String q) {
        List<EventResponseDTO> events = eventService.searchEventsByLocation(q);
        return ApiResponse.ok("Events found", events);
    }

    /**
     * POST /api/events - Create a new event
     */
    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody CreateEventDTO eventDTO) {
        EventResponseDTO createdEvent = eventService.createEvent(eventDTO);
        return ApiResponse.created("Event created successfully", createdEvent);
    }

    /**
     * PUT /api/events/{id} - Update an existing event
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventDTO eventDTO) {
        return eventService.updateEvent(id, eventDTO)
                .map(event -> ApiResponse.ok("Event updated successfully", event))
                .orElse(ApiResponse.notFound("Event not found with id: " + id));
    }

    /**
     * DELETE /api/events/{id} - Delete an event
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        boolean deleted = eventService.deleteEvent(id);
        if (deleted) {
            return ApiResponse.ok("Event deleted successfully", (Void) null);
        }
        return ApiResponse.notFound("Event not found with id: " + id);
    }

    // ==================== EVENT INVITES ENDPOINTS ====================

    /**
     * POST /api/events/{eventId}/invite/{userId} - Invite a user to an event
     */
    @PostMapping("/{eventId}/invite/{userId}")
    public ResponseEntity<?> inviteUserToEvent(
            @PathVariable Long eventId,
            @PathVariable Long userId) {
        try {
            EventInviteResponseDTO invite = eventService.inviteUserToEvent(eventId, userId);
            return ApiResponse.created("User invited successfully", invite);
        } catch (IllegalArgumentException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (IllegalStateException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * GET /api/events/{eventId}/invites - Get all invites for an event
     */
    @GetMapping("/{eventId}/invites")
    public ResponseEntity<?> getEventInvites(@PathVariable Long eventId) {
        List<EventInviteResponseDTO> invites = eventService.getEventInvites(eventId);
        return ApiResponse.ok("Event invites retrieved successfully", invites);
    }

    /**
     * GET /api/events/invites/user/{userId} - Get all events a user is invited to
     */
    @GetMapping("/invites/user/{userId}")
    public ResponseEntity<?> getUserInvitedEvents(@PathVariable Long userId) {
        List<EventResponseDTO> events = eventService.getUserInvitedEvents(userId);
        return ApiResponse.ok("User invited events retrieved successfully", events);
    }

    /**
     * GET /api/events/invites/my - Get events the current user is invited to
     */
    @GetMapping("/invites/my")
    public ResponseEntity<?> getCurrentUserInvitedEvents() {
        List<EventResponseDTO> events = eventService.getCurrentUserInvitedEvents();
        return ApiResponse.ok("Your invited events retrieved successfully", events);
    }

    /**
     * DELETE /api/events/{eventId}/invite/{userId} - Remove an invite
     */
    @DeleteMapping("/{eventId}/invite/{userId}")
    public ResponseEntity<?> removeInvite(
            @PathVariable Long eventId,
            @PathVariable Long userId) {
        boolean removed = eventService.removeInvite(eventId, userId);
        if (removed) {
            return ApiResponse.ok("Invite removed successfully", (Void) null);
        }
        return ApiResponse.notFound("Invite not found");
    }

    /**
     * GET /api/events/{eventId}/invited/{userId} - Check if a user is invited
     */
    @GetMapping("/{eventId}/invited/{userId}")
    public ResponseEntity<?> isUserInvited(
            @PathVariable Long eventId,
            @PathVariable Long userId) {
        boolean isInvited = eventService.isUserInvited(eventId, userId);
        return ApiResponse.ok("Invite status checked", isInvited);
    }
}
