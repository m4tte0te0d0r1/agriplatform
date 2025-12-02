package it.unicam.cs.ids.agriplatform.controllers.marketplace;

import it.unicam.cs.ids.agriplatform.dto.input.marketplace.CreateMarketplaceListingDTO;
import it.unicam.cs.ids.agriplatform.dto.input.marketplace.UpdateMarketplaceListingDTO;
import it.unicam.cs.ids.agriplatform.dto.output.MarketplaceListingResponseDTO;
import it.unicam.cs.ids.agriplatform.models.MarketplaceItemType;
import it.unicam.cs.ids.agriplatform.services.MarketplaceService;
import it.unicam.cs.ids.agriplatform.utils.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketplace")
public class MarketplaceController {

    private final MarketplaceService marketplaceService;

    @Autowired
    public MarketplaceController(MarketplaceService marketplaceService) {
        this.marketplaceService = marketplaceService;
    }

    /**
     * GET /api/marketplace - Get all available listings
     */
    @GetMapping
    public ResponseEntity<?> getAvailableListings() {
        List<MarketplaceListingResponseDTO> listings = marketplaceService.getAvailableListings();
        return ApiResponse.ok("Listings retrieved successfully", listings);
    }

    /**
     * GET /api/marketplace/type/{type} - Get listings by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getAvailableListingsByType(@PathVariable MarketplaceItemType type) {
        List<MarketplaceListingResponseDTO> listings = marketplaceService.getAvailableListingsByType(type);
        return ApiResponse.ok("Listings retrieved successfully", listings);
    }

    /**
     * GET /api/marketplace/{id} - Get listing by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getListingById(@PathVariable Long id) {
        return marketplaceService.getListingById(id)
                .map(listing -> ApiResponse.ok("Listing retrieved successfully", listing))
                .orElse(ApiResponse.notFound("Listing not found with id: " + id));
    }

    /**
     * GET /api/marketplace/seller/{sellerId} - Get listings by seller
     */
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getListingsBySeller(@PathVariable Long sellerId) {
        List<MarketplaceListingResponseDTO> listings = marketplaceService.getListingsBySeller(sellerId);
        return ApiResponse.ok("Seller listings retrieved successfully", listings);
    }

    /**
     * GET /api/marketplace/my - Get current user's listings
     */
    @GetMapping("/my")
    public ResponseEntity<?> getCurrentUserListings() {
        List<MarketplaceListingResponseDTO> listings = marketplaceService.getCurrentUserListings();
        return ApiResponse.ok("Your listings retrieved successfully", listings);
    }

    /**
     * POST /api/marketplace - Create a new listing
     */
    @PostMapping
    public ResponseEntity<?> createListing(@Valid @RequestBody CreateMarketplaceListingDTO dto) {
        try {
            MarketplaceListingResponseDTO created = marketplaceService.createListing(dto);
            return ApiResponse.created("Listing created successfully", created);
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * PUT /api/marketplace/{id} - Update a listing
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateListing(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMarketplaceListingDTO dto) {
        try {
            return marketplaceService.updateListing(id, dto)
                    .map(listing -> ApiResponse.ok("Listing updated successfully", listing))
                    .orElse(ApiResponse.notFound("Listing not found with id: " + id));
        } catch (IllegalStateException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * DELETE /api/marketplace/{id} - Delete a listing
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteListing(@PathVariable Long id) {
        try {
            boolean deleted = marketplaceService.deleteListing(id);
            if (deleted) {
                return ApiResponse.ok("Listing deleted successfully", (Void) null);
            }
            return ApiResponse.notFound("Listing not found with id: " + id);
        } catch (IllegalStateException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }
}
