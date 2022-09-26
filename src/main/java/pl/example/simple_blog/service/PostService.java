package pl.example.simple_blog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.example.simple_blog.model.Post;
import pl.example.simple_blog.repository.PostRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public void addPost(Post post) {
        postRepository.save(post);
    }

    public List<Post> listAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> postsById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if(postOptional.isPresent()){
            return postOptional;
        }
        throw new EntityNotFoundException("Unable to find post with id: " + postId);

    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public void updatePost(Long postId, Post editPost) {
        LocalDateTime now = LocalDateTime.now();
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            if (editPost.getTitle() != null) {
                post.setTitle(editPost.getTitle());
            }
            if (editPost.getTitle() != null) {
                post.setContent(editPost.getContent());
            }
            if (editPost.getUpdateDate() != null) {
                post.setUpdateDate(now);
            }
            postRepository.save(post);
            return;
        }
        throw new EntityNotFoundException("Unable to find post with id: " + postId);
    }


}