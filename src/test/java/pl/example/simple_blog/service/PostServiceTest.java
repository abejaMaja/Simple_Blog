package pl.example.simple_blog.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.example.simple_blog.model.Post;
import pl.example.simple_blog.model.PostDTO;
import pl.example.simple_blog.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;


@SpringBootTest
@RequiredArgsConstructor
class PostServiceTest {
    private PostService postService;
    private PostRepository postRepository;

    @Test
    public void test_findAll(){
        LocalDateTime timeNow = LocalDateTime.now();
        postRepository = mock(PostRepository.class);
        postService = new PostServiceDBImpl(postRepository);
        List<Post> postList = new ArrayList<>();
        postList.add(new Post(1L, "Title1", "body1", timeNow, timeNow ));
        postList.add(new Post(2L, "Title2", "body2", timeNow, timeNow ));

        when(postRepository.findAll()).thenReturn(postList);

        List<PostDTO> expectedList = postService.listAllPosts();

        Assertions.assertEquals(2, expectedList.size());
    }
    @Test
    void test_FindById() {
        LocalDateTime timeNow = LocalDateTime.now();
        postRepository = mock(PostRepository.class);
        postService = new PostServiceDBImpl(postRepository);
        Post post1 = new Post(1L, "Title1", "body1", timeNow, timeNow );
        doReturn(Optional.of(post1)).when(postRepository).findById(1L);

        Optional<Post> optionalPost = postService.postsById(1l);
        Post post = optionalPost.get();

        Assertions.assertTrue(optionalPost.isPresent());
        Assertions.assertEquals("Title1", post.getTitle());

    }


}







