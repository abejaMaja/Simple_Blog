package pl.example.simple_blog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.example.simple_blog.model.Post;
import pl.example.simple_blog.model.PostDTO;
import pl.example.simple_blog.repository.PostRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceDBImpl implements PostService {
    private final PostRepository postRepository;
    @Override
    public void addPost(Post post) {
        postRepository.save(post);
    }
    @Override
    public List<PostDTO> listAllPosts() {
        List<Post> postList = postRepository.findAll();

        List<PostDTO> posts = new ArrayList<>();
        for (Post post : postList) {
            posts.add(post.mapToPostDTO());
        }

        return posts;
    }

    @Override
    public Optional<Post> postsById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if(postOptional.isPresent()){
            return postOptional;
        }
        throw new EntityNotFoundException("Unable to find post with id: " + postId);

    }
    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
    @Override
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