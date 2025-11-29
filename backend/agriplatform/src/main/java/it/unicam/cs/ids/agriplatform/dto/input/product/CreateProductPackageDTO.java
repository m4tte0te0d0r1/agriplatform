package it.unicam.cs.ids.agriplatform.dto.input.product;

import java.util.List;

public record CreateProductPackageDTO(
        String name,
        double price,
        String description,
        List<Long> productIds) {
}
