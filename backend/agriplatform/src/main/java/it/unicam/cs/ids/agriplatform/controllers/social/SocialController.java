package it.unicam.cs.ids.agriplatform.controllers.social;

import it.unicam.cs.ids.agriplatform.dto.output.SocialFeedResponseDTO;
import it.unicam.cs.ids.agriplatform.services.SocialService;
import it.unicam.cs.ids.agriplatform.utils.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/social")
public class SocialController {

    private final SocialService socialService;

    @Autowired
    public SocialController(SocialService socialService) {
        this.socialService = socialService;
    }

    /**
     * GET /api/social/feed - Get social feed (all posts)
     */
    @GetMapping("/feed")
    public ResponseEntity<?> getSocialFeed() {
        SocialFeedResponseDTO feed = socialService.getSocialFeed();
        return ApiResponse.ok("Social feed retrieved successfully", feed);
    }

    /**
     * GET /api/social/{id} - Get social feed by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSocialFeedById(@PathVariable Long id) {
        return socialService.getSocialFeedById(id)
                .map(feed -> ApiResponse.ok("Social feed retrieved successfully", feed))
                .orElse(ApiResponse.notFound("Social feed not found with id: " + id));
    }
}
