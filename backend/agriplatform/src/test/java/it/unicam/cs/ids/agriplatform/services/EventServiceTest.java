package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.input.event.CreateEventDTO;
import it.unicam.cs.ids.agriplatform.dto.output.EventInviteResponseDTO;
import it.unicam.cs.ids.agriplatform.dto.output.EventResponseDTO;
import it.unicam.cs.ids.agriplatform.models.Event;
import it.unicam.cs.ids.agriplatform.models.EventInvites;
import it.unicam.cs.ids.agriplatform.repositories.EventRepository;
import it.unicam.cs.ids.agriplatform.repositories.EventInvitesRepository;
import it.unicam.cs.ids.agriplatform.utils.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventInvitesRepository eventInvitesRepository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Event(1L, "Test Event", "Description", LocalDateTime.now().plusDays(5), "Milano");
    }

    @Test
    void testCreateEvent_Success() {
        // Given
        CreateEventDTO dto = new CreateEventDTO(
                "Fiera Agricola",
                "Grande evento",
                LocalDateTime.now().plusDays(10),
                "Roma");

        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // When
        EventResponseDTO result = eventService.createEvent(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(testEvent.getName());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testInviteUserToEvent_Success() {
        // Given
        Long eventId = 1L;
        Long userId = 100L;
        EventInvites savedInvite = new EventInvites(1L, eventId, userId);

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventInvitesRepository.existsByEventIdAndUserId(eventId, userId)).thenReturn(false);
        when(eventInvitesRepository.save(any(EventInvites.class))).thenReturn(savedInvite);

        // When
        EventInviteResponseDTO result = eventService.inviteUserToEvent(eventId, userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.eventId()).isEqualTo(eventId);
        assertThat(result.userId()).isEqualTo(userId);
        verify(eventInvitesRepository, times(1)).save(any(EventInvites.class));
    }

    @Test
    void testInviteUserToEvent_EventNotFound() {
        // Given
        Long eventId = 999L;
        Long userId = 100L;

        when(eventRepository.existsById(eventId)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> eventService.inviteUserToEvent(eventId, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Event not found");

        verify(eventInvitesRepository, never()).save(any());
    }

    @Test
    void testInviteUserToEvent_UserAlreadyInvited() {
        // Given
        Long eventId = 1L;
        Long userId = 100L;

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventInvitesRepository.existsByEventIdAndUserId(eventId, userId)).thenReturn(true);

        // When/Then
        assertThatThrownBy(() -> eventService.inviteUserToEvent(eventId, userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already invited");

        verify(eventInvitesRepository, never()).save(any());
    }

    @Test
    void testDeleteEvent_WithCascadeDeleteInvites() {
        // Given
        Long eventId = 1L;

        when(eventRepository.existsById(eventId)).thenReturn(true);
        doNothing().when(eventInvitesRepository).deleteByEventId(eventId);
        doNothing().when(eventRepository).deleteById(eventId);

        // When
        boolean result = eventService.deleteEvent(eventId);

        // Then
        assertThat(result).isTrue();
        verify(eventInvitesRepository, times(1)).deleteByEventId(eventId);
        verify(eventRepository, times(1)).deleteById(eventId);
    }
}
