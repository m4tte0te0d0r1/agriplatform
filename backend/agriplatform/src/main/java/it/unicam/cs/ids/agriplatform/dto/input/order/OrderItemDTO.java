package it.unicam.cs.ids.agriplatform.dto.input.order;

import it.unicam.cs.ids.agriplatform.models.OrderItemType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemDTO(

        @NotNull(message = "Item type is required") OrderItemType itemType,

        @NotNull(message = "Item ID is required") Long itemId,

        @Min(value = 1, message = "Quantity must be at least 1") int quantity

) {
}
