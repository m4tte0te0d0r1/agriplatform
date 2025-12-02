package it.unicam.cs.ids.agriplatform.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.cs.ids.agriplatform.dto.input.event.CreateEventDTO;
import it.unicam.cs.ids.agriplatform.models.Event;
import it.unicam.cs.ids.agriplatform.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Disable security for simpler testing
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
    }

    @Test
    void testGetAllEvents_ReturnsEventsList() throws Exception {
        // Given: eventi nel database
        Event event1 = new Event(1, "Event 1", "Description 1", LocalDateTime.now().plusDays(1), "Milano");
        Event event2 = new Event(2, "Event 2", "Description 2", LocalDateTime.now().plusDays(5), "Roma");
        eventRepository.save(event1);
        eventRepository.save(event2);

        // When/Then: GET /api/events
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testCreateEvent_Success() throws Exception {
        // Given: DTO per creare evento
        CreateEventDTO dto = new CreateEventDTO(
                "Fiera Agricola 2025",
                "Grande evento agricolo",
                LocalDateTime.now().plusDays(30),
                "Verona");

        String jsonRequest = objectMapper.writeValueAsString(dto);

        // When/Then: POST /api/events
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Fiera Agricola 2025"))
                .andExpect(jsonPath("$.data.location").value("Verona"));
    }

    @Test
    void testGetEventById_NotFound() throws Exception {
        // When/Then: GET /api/events/999 (non esistente)
        mockMvc.perform(get("/api/events/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
