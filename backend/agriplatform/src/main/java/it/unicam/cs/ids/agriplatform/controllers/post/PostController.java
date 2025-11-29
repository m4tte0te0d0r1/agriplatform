package it.unicam.cs.ids.agriplatform.controllers.post;

import it.unicam.cs.ids.agriplatform.dto.input.post.CreatePostDTO;
import it.unicam.cs.ids.agriplatform.dto.input.post.UpdatePostDTO;
import it.unicam.cs.ids.agriplatform.dto.output.PostResponseDTO;
import it.unicam.cs.ids.agriplatform.services.PostService;
import it.unicam.cs.ids.agriplatform.utils.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * GET /api/posts - Get all posts
     */
    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ApiResponse.ok("Posts retrieved successfully", posts);
    }

    /**
     * GET /api/posts/{id} - Get a specific post by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(post -> ApiResponse.ok("Post retrieved successfully", post))
                .orElse(ApiResponse.notFound("Post not found with id: " + id));
    }

    /**
     * GET /api/posts/user/{userId} - Get all posts by a specific user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable Long userId) {
        List<PostResponseDTO> posts = postService.getPostsByUserId(userId);
        return ApiResponse.ok("User posts retrieved successfully", posts);
    }

    /**
     * GET /api/posts/my - Get all posts by the current authenticated user
     */
    @GetMapping("/my")
    public ResponseEntity<?> getCurrentUserPosts() {
        List<PostResponseDTO> posts = postService.getCurrentUserPosts();
        return ApiResponse.ok("Your posts retrieved successfully", posts);
    }

    /**
     * POST /api/posts - Create a new post
     */
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostDTO postDTO) {
        PostResponseDTO createdPost = postService.createPost(postDTO);
        return ApiResponse.created("Post created successfully", createdPost);
    }

    /**
     * PUT /api/posts/{id} - Update an existing post
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostDTO postDTO) {
        return postService.updatePost(id, postDTO)
                .map(post -> ApiResponse.ok("Post updated successfully", post))
                .orElse(ApiResponse.notFound("Post not found with id: " + id));
    }

    /**
     * DELETE /api/posts/{id} - Delete a post
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        boolean deleted = postService.deletePost(id);
        if (deleted) {
            return ApiResponse.ok("Post deleted successfully", (Void) null);
        }
        return ApiResponse.notFound("Post not found with id: " + id);
    }
}
