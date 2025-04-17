package it.unicam.cs.ids.agriplatform.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import it.unicam.cs.ids.agriplatform.dto.input.auth.SignInDTO;
import it.unicam.cs.ids.agriplatform.dto.output.auth.SignInResponseDTO;
import it.unicam.cs.ids.agriplatform.dto.input.auth.SignUpDTO;
import it.unicam.cs.ids.agriplatform.dto.output.auth.SignUpResponseDTO;
import it.unicam.cs.ids.agriplatform.models.Role;
import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.UserRepository;
import it.unicam.cs.ids.agriplatform.security.JwtUtil;
import it.unicam.cs.ids.agriplatform.services.AuthService;
import it.unicam.cs.ids.agriplatform.utils.ApiResponse;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(
            AuthService authService,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Endpoint for user authentication.
     *
     * @param signInRequest DTO containing email and password.
     * @return ResponseEntity with JWT if authentication succeeds.
     */
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInDTO signInRequest) {
        Optional<User> userOptional = authService.authenticate(signInRequest.email(), signInRequest.password());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            String jwt = jwtUtil.generateToken(user);

            SignInResponseDTO response = new SignInResponseDTO(
                jwt
            );

            return ApiResponse.ok("Authentication successful", response);
        } else {
            return ApiResponse.unauthorized("Invalid credentials");
        }
    }

    /**
     * Endpoint for user registration.
     *
     * @param signUpRequest DTO containing user details for registration.
     * @return ResponseEntity with user data if registration succeeds.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDTO signUpRequest) {
        Optional<User> existingUser = userRepository.findByEmail(signUpRequest.email());
        if (existingUser.isPresent()) {
            return ApiResponse.badRequest("Email already in use");
        }

        User user = new User(
            0,
            signUpRequest.username(),
            signUpRequest.email(),
            passwordEncoder.encode(signUpRequest.password()),
            Role.GENERIC_USER
        );

        User savedUser = userRepository.save(user);

        SignUpResponseDTO response = new SignUpResponseDTO(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getRole()
        );

        return ApiResponse.created("User registered successfully", response);
    }
}
