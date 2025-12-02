package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.Marketplace;
import it.unicam.cs.ids.agriplatform.models.MarketplaceItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketplaceRepository extends JpaRepository<Marketplace, Long> {

    /**
     * Find all available listings
     */
    List<Marketplace> findByIsAvailableTrue();

    /**
     * Find listings by seller
     */
    List<Marketplace> findBySellerId(Long sellerId);

    /**
     * Find available listings by seller
     */
    List<Marketplace> findBySellerIdAndIsAvailableTrue(Long sellerId);

    /**
     * Find listings by item type
     */
    List<Marketplace> findByItemTypeAndIsAvailableTrue(MarketplaceItemType itemType);

    /**
     * Find listing by item type and item ID
     */
    List<Marketplace> findByItemTypeAndItemId(MarketplaceItemType itemType, Long itemId);
}
