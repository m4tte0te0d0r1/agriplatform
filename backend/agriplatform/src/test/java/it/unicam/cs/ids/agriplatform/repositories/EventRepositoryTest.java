package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
    }

    @Test
    void testFindUpcomingEvents() {
        // Given: eventos futuri e passati
        LocalDateTime now = LocalDateTime.now();
        Event pastEvent = new Event(1, "Past Event", "Passato", now.minusDays(5), "Milano");
        Event futureEvent1 = new Event(2, "Future Event 1", "Futuro 1", now.plusDays(2), "Roma");
        Event futureEvent2 = new Event(3, "Future Event 2", "Futuro 2", now.plusDays(10), "Torino");

        eventRepository.save(pastEvent);
        eventRepository.save(futureEvent1);
        eventRepository.save(futureEvent2);

        // When: cerco eventi futuri
        List<Event> upcomingEvents = eventRepository.findByEventDateAfterOrderByEventDateAsc(now);

        // Then: solo eventi futuri, ordinati per data
        assertThat(upcomingEvents).hasSize(2);
        assertThat(upcomingEvents.get(0).getName()).isEqualTo("Future Event 1");
        assertThat(upcomingEvents.get(1).getName()).isEqualTo("Future Event 2");
    }

    @Test
    void testFindPastEvents() {
        // Given: eventi futuri e passati
        LocalDateTime now = LocalDateTime.now();
        Event pastEvent1 = new Event(4, "Past Event 1", "Passato 1", now.minusDays(10), "Milano");
        Event pastEvent2 = new Event(5, "Past Event 2", "Passato 2", now.minusDays(2), "Roma");
        Event futureEvent = new Event(6, "Future Event", "Futuro", now.plusDays(5), "Torino");

        eventRepository.save(pastEvent1);
        eventRepository.save(pastEvent2);
        eventRepository.save(futureEvent);

        // When: cerco eventi passati
        List<Event> pastEvents = eventRepository.findByEventDateBeforeOrderByEventDateDesc(now);

        // Then: solo eventi passati, ordinati per data discendente
        assertThat(pastEvents).hasSize(2);
        assertThat(pastEvents.get(0).getName()).isEqualTo("Past Event 2");
        assertThat(pastEvents.get(1).getName()).isEqualTo("Past Event 1");
    }

    @Test
    void testSearchEventsByName() {
        // Given: eventi con nomi diversi
        LocalDateTime now = LocalDateTime.now();
        Event fiera1 = new Event(7, "Fiera Agricola Milano", "Descrizione", now.plusDays(1), "Milano");
        Event fiera2 = new Event(8, "Grande Fiera Roma", "Descrizione", now.plusDays(5), "Roma");
        Event workshop = new Event(9, "Workshop Biologico", "Descrizione", now.plusDays(3), "Torino");

        eventRepository.save(fiera1);
        eventRepository.save(fiera2);
        eventRepository.save(workshop);

        // When: cerco eventi con "fiera" nel nome (case insensitive)
        List<Event> results = eventRepository.findByNameContainingIgnoreCaseOrderByEventDateAsc("fiera");

        // Then: trovo solo gli eventi con "fiera" nel nome
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getName()).isEqualTo("Fiera Agricola Milano");
        assertThat(results.get(1).getName()).isEqualTo("Grande Fiera Roma");
    }
}
