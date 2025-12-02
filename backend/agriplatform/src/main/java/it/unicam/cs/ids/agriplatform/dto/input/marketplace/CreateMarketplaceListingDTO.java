package it.unicam.cs.ids.agriplatform.dto.input.marketplace;

import it.unicam.cs.ids.agriplatform.models.MarketplaceItemType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateMarketplaceListingDTO(

        @NotNull(message = "Item type is required") MarketplaceItemType itemType,

        @NotNull(message = "Item ID is required") Long itemId,

        @NotNull(message = "Price is required") @Min(value = 0, message = "Price must be positive") Double price

) {
}
