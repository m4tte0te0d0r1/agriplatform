package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.EventInvites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventInvitesRepository extends JpaRepository<EventInvites, Long> {

    /**
     * Find all invites for a specific event
     */
    List<EventInvites> findByEventId(Long eventId);

    /**
     * Find all invites for a specific user
     */
    List<EventInvites> findByUserId(Long userId);

    /**
     * Find a specific invite by event and user
     */
    Optional<EventInvites> findByEventIdAndUserId(Long eventId, Long userId);

    /**
     * Delete all invites for a specific event
     */
    void deleteByEventId(Long eventId);

    /**
     * Delete a specific invite by event and user
     */
    void deleteByEventIdAndUserId(Long eventId, Long userId);

    /**
     * Check if a user is invited to an event
     */
    boolean existsByEventIdAndUserId(Long eventId, Long userId);
}
