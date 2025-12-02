package it.unicam.cs.ids.agriplatform.dto.output;

import java.util.List;

public record SocialFeedResponseDTO(
        Long id,
        List<PostResponseDTO> posts) {
}
