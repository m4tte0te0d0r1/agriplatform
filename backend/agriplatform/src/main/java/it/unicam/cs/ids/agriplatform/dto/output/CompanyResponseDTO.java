package it.unicam.cs.ids.agriplatform.dto.output;

public record CompanyResponseDTO(
        Long id,
        String name,
        String address,
        Long userId,
        String latitude,
        String longitude) {
}
