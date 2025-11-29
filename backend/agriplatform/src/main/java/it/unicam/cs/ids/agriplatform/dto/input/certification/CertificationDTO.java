package it.unicam.cs.ids.agriplatform.dto.input.certification;

import jakarta.validation.constraints.NotNull;

public record CertificationDTO(

    @NotNull(message = "User ID is required")
    boolean isApproved
) {}
