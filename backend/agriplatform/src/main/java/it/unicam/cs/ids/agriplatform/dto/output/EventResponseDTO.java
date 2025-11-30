package it.unicam.cs.ids.agriplatform.dto.output;

import java.time.LocalDateTime;

public record EventResponseDTO(
        Long id,
        String name,
        String description,
        LocalDateTime eventDate,
        String location) {
}
