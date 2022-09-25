package pl.example.simple_blog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.example.simple_blog.model.Post;
import pl.example.simple_blog.repository.PostRepository;

import java.util.List;

@Slf4j
@Component
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
}
