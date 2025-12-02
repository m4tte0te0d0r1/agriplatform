package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.Social;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepository extends JpaRepository<Social, Long> {
    // Basic CRUD operations provided by JpaRepository
}
