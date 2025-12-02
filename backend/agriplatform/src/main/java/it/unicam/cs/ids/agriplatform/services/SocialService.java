package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.output.PostResponseDTO;
import it.unicam.cs.ids.agriplatform.dto.output.SocialFeedResponseDTO;
import it.unicam.cs.ids.agriplatform.models.Post;
import it.unicam.cs.ids.agriplatform.repositories.PostRepository;
import it.unicam.cs.ids.agriplatform.repositories.SocialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SocialService {

    private final SocialRepository socialRepository;
    private final PostRepository postRepository;

    @Autowired
    public SocialService(SocialRepository socialRepository, PostRepository postRepository) {
        this.socialRepository = socialRepository;
        this.postRepository = postRepository;
    }

    /**
     * Get social feed (all posts)
     */
    public SocialFeedResponseDTO getSocialFeed() {
        // Get all posts ordered by date
        List<Post> allPosts = postRepository.findAllByOrderByDateDesc();

        List<PostResponseDTO> postDTOs = allPosts.stream()
                .map(this::mapPostToDTO)
                .collect(Collectors.toList());

        return new SocialFeedResponseDTO(1L, postDTOs);
    }

    /**
     * Get social feed by ID
     */
    public Optional<SocialFeedResponseDTO> getSocialFeedById(Long id) {
        return socialRepository.findById(id).map(social -> {
            List<PostResponseDTO> postDTOs = social.getPosts().stream()
                    .map(this::mapPostToDTO)
                    .collect(Collectors.toList());

            return new SocialFeedResponseDTO(social.getId(), postDTOs);
        });
    }

    /**
     * Map Post to DTO
     */
    private PostResponseDTO mapPostToDTO(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getText(),
                post.getUserId(),
                post.getDate());
    }
}
