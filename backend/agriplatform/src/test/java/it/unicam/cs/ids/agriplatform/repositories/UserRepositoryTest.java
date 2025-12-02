package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.Role;
import it.unicam.cs.ids.agriplatform.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testFindByEmail_Success() {
        // Given: utente salvato
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);

        // When: cerco per email
        Optional<User> found = userRepository.findByEmail("test@example.com");

        // Then: utente trovato
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void testFindByUsername_Success() {
        // Given: utente salvato
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("johndoe");
        user.setPassword("encodedPassword");
        user.setRole(Role.PRODUCER);

        userRepository.save(user);

        // When: cerco per username
        Optional<User> found = userRepository.findByUsername("johndoe");

        // Then: utente trovato
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getRole()).isEqualTo(Role.PRODUCER);
    }

    @Test
    void testEmailUniqueness() {
        // Given: primo utente
        User user1 = new User();
        user1.setEmail("same@example.com");
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setRole(Role.CUSTOMER);

        userRepository.save(user1);

        // When: cerco per email
        Optional<User> found = userRepository.findByEmail("same@example.com");

        // Then: solo un utente con quella email
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("user1");
    }
}
