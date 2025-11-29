package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Find all posts by user ID
     */
    List<Post> findByUserId(Long userId);

    /**
     * Find all posts ordered by date descending (most recent first)
     */
    List<Post> findAllByOrderByDateDesc();

    /**
     * Find posts by user ID ordered by date descending
     */
    List<Post> findByUserIdOrderByDateDesc(Long userId);
}
