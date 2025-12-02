package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    /**
     * Find companies by user ID
     */
    List<Company> findByUserId(Long userId);

    /**
     * Find companies by name (case insensitive search)
     */
    List<Company> findByNameContainingIgnoreCase(String name);

    /**
     * Find companies by address (case insensitive search)
     */
    List<Company> findByAddressContainingIgnoreCase(String address);
}
