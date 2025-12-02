package it.unicam.cs.ids.agriplatform.dto.input.company;

import jakarta.validation.constraints.NotBlank;

public record UpdateCompanyDTO(

        @NotBlank(message = "Company name is required") String name,

        @NotBlank(message = "Address is required") String address,

        String latitude,

        String longitude

) {
}
