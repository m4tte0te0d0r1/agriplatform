package it.unicam.cs.ids.agriplatform.dto.input.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateEventDTO(

        @NotBlank(message = "Event name is required") String name,

        @NotBlank(message = "Description is required") String description,

        @NotNull(message = "Event date is required") LocalDateTime eventDate,

        @NotBlank(message = "Location is required") String location

) {
}
