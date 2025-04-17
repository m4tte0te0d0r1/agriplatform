package it.unicam.cs.ids.agriplatform.dto.input.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record UpdateProductDTO(

    @NotNull(message = "ID is required")
    @Min(value = 1, message = "ID must be at least 1")
    Long id,

    @NotBlank(message = "Product name is required")
    String name,

    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be at least 1")
    Long userId,

    @NotEmpty(message = "At least one product detail is required")
    List<@Valid ProductDetailDTO> details,

    @Min(value = 1, message = "Quantity must be at least 1")
    int quantity,

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    double price

) {}
