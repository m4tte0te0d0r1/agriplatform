package it.unicam.cs.ids.agriplatform.dto.input.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderDTO(

        @NotEmpty(message = "At least one item is required") List<@Valid OrderItemDTO> items,

        @NotBlank(message = "Delivery address is required") String deliveryAddress,

        String notes

) {
}
