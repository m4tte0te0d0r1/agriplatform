package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Find all events ordered by event date ascending (upcoming events first)
     */
    List<Event> findAllByOrderByEventDateAsc();

    /**
     * Find events after a specific date (upcoming events)
     */
    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDateTime date);

    /**
     * Find events before a specific date (past events)
     */
    List<Event> findByEventDateBeforeOrderByEventDateDesc(LocalDateTime date);

    /**
     * Find events by name containing (case insensitive search)
     */
    List<Event> findByNameContainingIgnoreCaseOrderByEventDateAsc(String name);

    /**
     * Find events by location containing (case insensitive search)
     */
    List<Event> findByLocationContainingIgnoreCaseOrderByEventDateAsc(String location);
}
