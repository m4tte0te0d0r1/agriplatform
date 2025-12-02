package it.unicam.cs.ids.agriplatform.dto.input.certification;

import jakarta.validation.constraints.NotBlank;

public record CreateCertificationDTO(

        @NotBlank(message = "Certification name is required") String name,

        @NotBlank(message = "Description is required") String description,

        @NotBlank(message = "Issuer is required") String issuer

) {
}
