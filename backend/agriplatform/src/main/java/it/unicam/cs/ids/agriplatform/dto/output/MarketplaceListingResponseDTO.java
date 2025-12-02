package it.unicam.cs.ids.agriplatform.dto.output;

import it.unicam.cs.ids.agriplatform.models.MarketplaceItemType;

public record MarketplaceListingResponseDTO(
        Long id,
        MarketplaceItemType itemType,
        Long itemId,
        Long sellerId,
        boolean isAvailable,
        double price) {
}
