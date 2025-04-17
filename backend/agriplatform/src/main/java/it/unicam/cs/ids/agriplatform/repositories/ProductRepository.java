package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Product entities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find a product by its name.
     *
     * @param name the product name
     * @return an Optional containing the product if found, or empty if not
     */
    Optional<Product> findByName(String name);

    /**
     * Find all products that are marked as available.
     *
     * @return a list of available products
     */
    List<Product> findByAvailableTrue();

    /**
     * Find all products within a certain price range.
     *
     * @param min minimum price (inclusive)
     * @param max maximum price (inclusive)
     * @return a list of products in the specified price range
     */
    List<Product> findByPriceBetween(double min, double max);

    /**
     * Check if a product with the given name exists.
     *
     * @param name the product name
     * @return true if a product with the name exists
     */
    boolean existsByName(String name);
}
