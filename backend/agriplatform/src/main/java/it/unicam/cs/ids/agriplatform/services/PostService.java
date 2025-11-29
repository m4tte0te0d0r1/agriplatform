package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.input.post.CreatePostDTO;
import it.unicam.cs.ids.agriplatform.dto.input.post.UpdatePostDTO;
import it.unicam.cs.ids.agriplatform.dto.output.PostResponseDTO;
import it.unicam.cs.ids.agriplatform.models.Post;
import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.PostRepository;
import it.unicam.cs.ids.agriplatform.utils.UserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserContext userContext;

    @Autowired
    public PostService(PostRepository postRepository, UserContext userContext) {
        this.postRepository = postRepository;
        this.userContext = userContext;
    }

    /**
     * Get all posts ordered by date (most recent first)
     */
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAllByOrderByDateDesc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a post by its ID
     */
    public Optional<PostResponseDTO> getPostById(Long id) {
        return postRepository.findById(id)
                .map(this::mapToDTO);
    }

    /**
     * Get all posts by a specific user
     */
    public List<PostResponseDTO> getPostsByUserId(Long userId) {
        return postRepository.findByUserIdOrderByDateDesc(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all posts by the current authenticated user
     */
    public List<PostResponseDTO> getCurrentUserPosts() {
        User currentUser = userContext.getCurrentUser();
        return getPostsByUserId(currentUser.getId());
    }

    /**
     * Create a new post
     */
    public PostResponseDTO createPost(CreatePostDTO postDTO) {
        User currentUser = userContext.getCurrentUser();

        Post post = new Post(
                0L, // ID will be generated
                postDTO.title(),
                postDTO.text(),
                currentUser.getId(),
                LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        return mapToDTO(savedPost);
    }

    /**
     * Update an existing post
     */
    public Optional<PostResponseDTO> updatePost(Long id, UpdatePostDTO postDTO) {
        User currentUser = userContext.getCurrentUser();

        return postRepository.findById(id).map(existingPost -> {
            // Check if the current user is the owner of the post
            if (existingPost.getUserId() != currentUser.getId()) {
                throw new IllegalStateException("You can only update your own posts");
            }

            existingPost.setTitle(postDTO.title());
            existingPost.setText(postDTO.text());

            Post updatedPost = postRepository.save(existingPost);
            return mapToDTO(updatedPost);
        });
    }

    /**
     * Delete a post by ID
     */
    public boolean deletePost(Long id) {
        User currentUser = userContext.getCurrentUser();

        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            // Check if the current user is the owner of the post
            if (post.get().getUserId() != currentUser.getId()) {
                throw new IllegalStateException("You can only delete your own posts");
            }

            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Map Post entity to PostResponseDTO
     */
    private PostResponseDTO mapToDTO(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getText(),
                post.getUserId(),
                post.getDate());
    }
}
