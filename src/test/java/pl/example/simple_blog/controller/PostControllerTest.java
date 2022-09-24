package pl.example.simple_blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import pl.example.simple_blog.SimpleBlogApplication;
import pl.example.simple_blog.model.Post;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = SimpleBlogApplication.class)
class PostControllerTest {

    @LocalServerPort
    public int randomServerPort;

    @Test
    public void test_emptyListAfterStartDoesNotThrowErrorsAndReturnsStatusCodeOK(){
        TestRestTemplate testRestTemplate = new TestRestTemplate();

        ResponseEntity<List<Post>> responseEntity = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/post", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Post>>() {
        });

        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // content verification
        List<Post> postList = responseEntity.getBody();
        Assertions.assertEquals(0, postList.size());
    }

    @SneakyThrows
    @Test
    public void test_addFirstObjectDoesNotThrowErrorsAndReturnsStatusCodeCreated() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();

        Post post = new Post ();
        post.setTitle("First title");
        post.setContent("Body of the post");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(post);

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        String result = testRestTemplate.postForObject("http://localhost:" + randomServerPort + "/api/post", request, String.class);

        ResponseEntity<List<Post>> responseEntity = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/post", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Post>>() {
        });

        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // content verification
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("First title", post.getTitle());
        Assertions.assertEquals("Body of the post", post.getContent());

    }

}