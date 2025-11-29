package it.unicam.cs.ids.agriplatform.dto.input.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostDTO(

        @NotBlank(message = "Title is required") @Size(max = 100, message = "Title must not exceed 100 characters") String title,

        @NotBlank(message = "Text is required") String text

) {
}
