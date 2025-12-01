package it.unicam.cs.ids.agriplatform.dto.input.order;

import it.unicam.cs.ids.agriplatform.models.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusDTO(

        @NotNull(message = "Status is required") OrderStatus status

) {
}
