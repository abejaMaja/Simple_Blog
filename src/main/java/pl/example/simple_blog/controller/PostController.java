package pl.example.simple_blog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.example.simple_blog.service.PostService;
import pl.example.simple_blog.model.Post;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService; // business layer

    @PostMapping("/add")
    public void addCustomer(@RequestBody Post post) {
        log.info("Post adding method was requested" + post);
        postService.addPost(post);
    }

    @GetMapping
    public List<Post> customerList() {
        log.info("Listing all posts");
        return postService.listAllPosts();
    }
}
