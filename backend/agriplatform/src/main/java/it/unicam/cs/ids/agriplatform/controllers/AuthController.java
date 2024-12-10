package it.unicam.cs.ids.agriplatform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unicam.cs.ids.agriplatform.dto.auth.SignInDto;
import it.unicam.cs.ids.agriplatform.dto.auth.SignUpDto;
import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.UserRepository;
import it.unicam.cs.ids.agriplatform.security.JwtUtil;
import it.unicam.cs.ids.agriplatform.utils.ApiResponse;

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

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInDto signInRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.email(), signInRequest.password())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(signInRequest.email());
        String jwt = jwtUtil.generateToken(userDetails);
        return ApiResponse.ok("Authentication successful", jwt);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpRequest) {
        // Check if the user already exists
        Optional<User> existingUser = userRepository.findByUsername(signUpRequest.email());
        if (existingUser.isPresent()) {
            return ApiResponse.badRequest("Email already in use");
        }

        // Create and save the new user
        User user = new User();
        user.setUsername(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        // user.setName(signUpRequest.name());
        // user.setLastName(signUpRequest.lastName());
        
        User savedUser = userRepository.save(user);
        return ApiResponse.created(savedUser);
    }
}
