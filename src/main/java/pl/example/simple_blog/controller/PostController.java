package pl.example.simple_blog.controller;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.example.simple_blog.service.PostService;
import pl.example.simple_blog.model.Post;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor

public class PostController {
    private final PostService postService; // business layer

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPost(@RequestBody Post post) {
        log.info("Post adding method was requested" + post);
        postService.addPost(post);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Post> postList() {
        log.info("Listing all posts");
        return postService.listAllPosts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Post> showPostById(@PathVariable(name = "id") Long postId) {
        log.info("Listing post with ID = " + postId );
        return postService.postsById(postId);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") Long postId) {
        log.info("Received request: delete -> " + postId);
        postService.deletePost(postId);
    }
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(@PathVariable(name = "id") Long postId, @RequestBody Post post) {
        log.info("Received request: update -> " + post);
        postService.updatePost(postId, post);
    }

}
