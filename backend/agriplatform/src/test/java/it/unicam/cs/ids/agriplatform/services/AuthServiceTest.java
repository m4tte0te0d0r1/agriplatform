package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("$2a$10$encodedPassword"); // Password già encodata
    }

    @Test
    void testAuthenticate_Success() {
        // Given: credenziali corrette
        String email = "test@example.com";
        String plainPassword = "myPassword123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(plainPassword, testUser.getPassword())).thenReturn(true);

        // When: autenticazione
        Optional<User> result = authService.authenticate(email, plainPassword);

        // Then: autenticazione riuscita
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(plainPassword, testUser.getPassword());
    }

    @Test
    void testAuthenticate_WrongPassword() {
        // Given: password errata
        String email = "test@example.com";
        String wrongPassword = "wrongPassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(wrongPassword, testUser.getPassword())).thenReturn(false);

        // When: autenticazione
        Optional<User> result = authService.authenticate(email, wrongPassword);

        // Then: autenticazione fallita
        assertThat(result).isEmpty();
        verify(passwordEncoder, times(1)).matches(wrongPassword, testUser.getPassword());
    }

    @Test
    void testAuthenticate_UserNotFound() {
        // Given: email non esistente
        String email = "nonexistent@example.com";
        String password = "anyPassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When: autenticazione
        Optional<User> result = authService.authenticate(email, password);

        // Then: autenticazione fallita
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}
