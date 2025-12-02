package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.input.marketplace.CreateMarketplaceListingDTO;
import it.unicam.cs.ids.agriplatform.dto.input.marketplace.UpdateMarketplaceListingDTO;
import it.unicam.cs.ids.agriplatform.dto.output.MarketplaceListingResponseDTO;
import it.unicam.cs.ids.agriplatform.models.Marketplace;
import it.unicam.cs.ids.agriplatform.models.MarketplaceItemType;
import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.MarketplaceRepository;
import it.unicam.cs.ids.agriplatform.repositories.ProductRepository;
import it.unicam.cs.ids.agriplatform.repositories.ProductPackageRepository;
import it.unicam.cs.ids.agriplatform.utils.UserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarketplaceService {

    private final MarketplaceRepository marketplaceRepository;
    private final ProductRepository productRepository;
    private final ProductPackageRepository productPackageRepository;
    private final UserContext userContext;

    @Autowired
    public MarketplaceService(MarketplaceRepository marketplaceRepository,
            ProductRepository productRepository,
            ProductPackageRepository productPackageRepository,
            UserContext userContext) {
        this.marketplaceRepository = marketplaceRepository;
        this.productRepository = productRepository;
        this.productPackageRepository = productPackageRepository;
        this.userContext = userContext;
    }

    /**
     * Get all available listings
     */
    public List<MarketplaceListingResponseDTO> getAvailableListings() {
        return marketplaceRepository.findByIsAvailableTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get available listings by type
     */
    public List<MarketplaceListingResponseDTO> getAvailableListingsByType(MarketplaceItemType itemType) {
        return marketplaceRepository.findByItemTypeAndIsAvailableTrue(itemType)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get listing by ID
     */
    public Optional<MarketplaceListingResponseDTO> getListingById(Long id) {
        return marketplaceRepository.findById(id)
                .map(this::mapToDTO);
    }

    /**
     * Get listings by seller
     */
    public List<MarketplaceListingResponseDTO> getListingsBySeller(Long sellerId) {
        return marketplaceRepository.findBySellerId(sellerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get current user's listings
     */
    public List<MarketplaceListingResponseDTO> getCurrentUserListings() {
        User currentUser = userContext.getCurrentUser();
        return getListingsBySeller(currentUser.getId());
    }

    /**
     * Create a new marketplace listing
     */
    public MarketplaceListingResponseDTO createListing(CreateMarketplaceListingDTO dto) {
        User currentUser = userContext.getCurrentUser();

        // Verify item exists
        if (dto.itemType() == MarketplaceItemType.PRODUCT) {
            if (!productRepository.existsById(dto.itemId())) {
                throw new IllegalArgumentException("Product not found with id: " + dto.itemId());
            }
        } else {
            if (!productPackageRepository.existsById(dto.itemId())) {
                throw new IllegalArgumentException("Package not found with id: " + dto.itemId());
            }
        }

        Marketplace listing = new Marketplace(
                0L,
                dto.itemType(),
                dto.itemId(),
                currentUser.getId(),
                true, // New listings are available by default
                dto.price());

        Marketplace saved = marketplaceRepository.save(listing);
        return mapToDTO(saved);
    }

    /**
     * Update a listing
     */
    public Optional<MarketplaceListingResponseDTO> updateListing(Long id, UpdateMarketplaceListingDTO dto) {
        User currentUser = userContext.getCurrentUser();

        return marketplaceRepository.findById(id).map(listing -> {
            // Check ownership
            if (listing.getSellerId() != currentUser.getId()) {
                throw new IllegalStateException("You can only update your own listings");
            }

            listing.setPrice(dto.price());
            listing.setAvailable(dto.isAvailable());

            Marketplace updated = marketplaceRepository.save(listing);
            return mapToDTO(updated);
        });
    }

    /**
     * Delete a listing
     */
    public boolean deleteListing(Long id) {
        User currentUser = userContext.getCurrentUser();

        Optional<Marketplace> listingOpt = marketplaceRepository.findById(id);
        if (listingOpt.isPresent()) {
            Marketplace listing = listingOpt.get();

            // Check ownership
            if (listing.getSellerId() != currentUser.getId()) {
                throw new IllegalStateException("You can only delete your own listings");
            }

            marketplaceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Map Marketplace entity to DTO
     */
    private MarketplaceListingResponseDTO mapToDTO(Marketplace listing) {
        return new MarketplaceListingResponseDTO(
                listing.getId(),
                listing.getItemType(),
                listing.getItemId(),
                listing.getSellerId(),
                listing.isAvailable(),
                listing.getPrice());
    }
}
