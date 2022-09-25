package pl.example.simple_blog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import pl.example.simple_blog.SimpleBlogApplication;
import pl.example.simple_blog.model.Post;
import pl.example.simple_blog.service.PostService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SimpleBlogApplication.class)
class PostControllerTest {

    @LocalServerPort
    public int randomServerPort;

    @Autowired
    private PostController controller;
    LocalDateTime now = LocalDateTime.now();

    @Test
    public void test_emptyListAfterStartDoesNotThrowErrorsAndReturnsStatusCodeOK() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();


        ResponseEntity<List<Post>> responseEntity = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/post", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Post>>() {
        });

        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // content verification
        List<Post> postList = responseEntity.getBody();
        Assertions.assertEquals(0, postList.size());
    }

    @Test
    public void test_addFirstObjectDoesNotThrowErrorsAndReturnsStatusCodeCreated() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();

        ResponseEntity<Void> addResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + randomServerPort + "/api/post/add",
                HttpMethod.POST,
                new HttpEntity<>(
                        Post.builder()
                                .title("First title")
                                .content("The body of content")
                                .build()
                ),
                Void.class);

        // status code verification
        Assertions.assertEquals(HttpStatus.CREATED, addResponseEntity.getStatusCode());

        // DB state verification
        ResponseEntity<List<Post>> responseEntity = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/post", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Post>>() {
        });

        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // content verification
        List<Post> post = responseEntity.getBody();

        Assertions.assertEquals(1, post.size());
        Post theOnlyElement = post.get(0);

        Assertions.assertEquals("First title", theOnlyElement.getTitle());
        Assertions.assertEquals("The body of content", theOnlyElement.getContent());
        Assertions.assertNull(theOnlyElement.getUpdateDate());

    }

}