package it.unicam.cs.ids.agriplatform.dto.input.product;

import jakarta.validation.constraints.*;

public record ProductDetailDTO(
        @NotBlank(message = "Name is required") String name,

        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") double price,

        @NotBlank(message = "Description is required") String description) {
}
