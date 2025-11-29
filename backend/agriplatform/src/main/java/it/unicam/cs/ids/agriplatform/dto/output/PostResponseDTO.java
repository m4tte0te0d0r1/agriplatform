package it.unicam.cs.ids.agriplatform.dto.output;

import java.time.LocalDateTime;

public record PostResponseDTO(
        Long id,
        String title,
        String text,
        Long userId,
        LocalDateTime date) {
}
