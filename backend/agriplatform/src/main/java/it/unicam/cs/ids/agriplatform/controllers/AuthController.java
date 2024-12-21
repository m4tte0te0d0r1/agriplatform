package it.unicam.cs.ids.agriplatform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import it.unicam.cs.ids.agriplatform.dto.auth.SignInDTO;
import it.unicam.cs.ids.agriplatform.dto.auth.SignUpDTO;
import it.unicam.cs.ids.agriplatform.models.Role;
import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.UserRepository;
import it.unicam.cs.ids.agriplatform.security.JwtUtil;
import it.unicam.cs.ids.agriplatform.utils.ApiResponse;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
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
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.email(), signInRequest.password())
            );
        } catch (BadCredentialsException e) {
            return ApiResponse.unauthorized("Invalid email or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(signInRequest.email());
        String jwt = jwtUtil.generateToken(userDetails);
        return ApiResponse.ok("Authentication successful", jwt);
    }

    /**
     * Endpoint for user registration.
     *
     * @param signUpRequest DTO containing user details for registration.
     * @return ResponseEntity with user data if registration succeeds.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDTO signUpRequest) {
        Optional<User> existingUser = userRepository.findByUsername(signUpRequest.email());
        if (existingUser.isPresent()) {
            return ApiResponse.badRequest("Email already in use");
        }

        // Create and save new user
        User user = new User(
                0,
                signUpRequest.username(),
                signUpRequest.email(),
                passwordEncoder.encode(signUpRequest.password()),
                // TODO da implementare logica
                Role.ADMIN
        );

        User savedUser = userRepository.save(user);
        return ApiResponse.created("User registered successfully", savedUser);
    }
}
