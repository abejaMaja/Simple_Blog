package pl.example.simple_blog.service;

import pl.example.simple_blog.model.Post;
import pl.example.simple_blog.model.PostDTO;

import java.util.List;
import java.util.Optional;

public interface PostService {
    void addPost(Post post);
    List<PostDTO> listAllPosts();
    Optional<Post> postsById(Long postId);
    void updatePost(Long postId, Post editPost);
    void deletePost(Long id);
}
