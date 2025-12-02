package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    
    /**
     * Find all approved certifications
     */
    List<Certification> findByIsApprovedTrue();
    
    /**
     * Find all pending certifications (not approved)
     */
    List<Certification> findByIsApprovedFalse();
    
    /**
     * Find certifications by issuer
     */
    List<Certification> findByIssuerContainingIgnoreCase(String issuer);
    
    /**
     * Find certifications by name
     */
    List<Certification> findByNameContainingIgnoreCase(String name);
}
