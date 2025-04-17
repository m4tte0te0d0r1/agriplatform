package it.unicam.cs.ids.agriplatform.dto.input.product;

import jakarta.validation.constraints.*;

public record ProductDetailDTO(

    @NotNull(message = "ID is required")
    @Min(value = 1, message = "ID must be at least 1")
    Long id,

    @NotBlank(message = "Name is required")
    String name,

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    double price,

    @NotBlank(message = "Description is required")
    String description,

    @NotNull(message = "User ID is required")
    Long userId,

    @NotNull(message = "Product ID is required")
    Long productId,

    @NotNull(message = "Approved cannot be null")
    boolean approved
) {}
