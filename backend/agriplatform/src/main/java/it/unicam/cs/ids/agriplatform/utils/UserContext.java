package it.unicam.cs.ids.agriplatform.utils;

import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    private final UserRepository userRepository;

    @Autowired
    public UserContext(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get the currently authenticated user.
     *
     * @return the current User entity
     * @throws IllegalStateException if no user is authenticated or user not found
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("No user is currently authenticated");
        }

        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByEmail(username) // Assuming username is email based on UserDetails implementation
                                                    // usually
                .or(() -> userRepository.findByUsername(username)) // Fallback if it's username
                .orElseThrow(() -> new IllegalStateException("User not found in database: " + username));
    }
}
