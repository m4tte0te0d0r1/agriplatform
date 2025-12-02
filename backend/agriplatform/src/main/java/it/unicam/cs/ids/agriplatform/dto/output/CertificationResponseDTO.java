package it.unicam.cs.ids.agriplatform.dto.output;

public record CertificationResponseDTO(
        Long id,
        String name,
        String description,
        String issuer,
        boolean isApproved) {
}
