package it.unicam.cs.ids.agriplatform.dto.output.auth;

import it.unicam.cs.ids.agriplatform.models.Role;

public record SignUpResponseDTO(long id, String username, String email, Role role) {}
