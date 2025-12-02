package it.unicam.cs.ids.agriplatform.dto.input.marketplace;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateMarketplaceListingDTO(

        @NotNull(message = "Price is required") @Min(value = 0, message = "Price must be positive") Double price,

        @NotNull(message = "Availability is required") Boolean isAvailable

) {
}
