package it.unicam.cs.ids.agriplatform.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Social {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "social_id")
    private List<Post> posts = new ArrayList<>();

    public Social() {
    }

    public void addPost(Post post) {
        if (post != null) {
            posts.add(post);
        }
    }

    public void removePost(Post post) {
        posts.remove(post);
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts != null ? posts : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    // Equals & HashCode basati su id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Social)) return false;
        Social social = (Social) o;
        return Objects.equals(id, social.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
