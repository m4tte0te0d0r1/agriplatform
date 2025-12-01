package it.unicam.cs.ids.agriplatform.dto.output;

import it.unicam.cs.ids.agriplatform.models.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        Long userId,
        LocalDateTime orderDate,
        double totalPrice,
        OrderStatus status,
        String deliveryAddress,
        String notes,
        List<OrderItemResponseDTO> items) {
}
