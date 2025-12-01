package it.unicam.cs.ids.agriplatform.dto.output;

import it.unicam.cs.ids.agriplatform.models.OrderItemType;

public record OrderItemResponseDTO(
        Long id,
        OrderItemType itemType,
        Long itemId,
        int quantity,
        double unitPrice,
        double totalPrice) {
}
