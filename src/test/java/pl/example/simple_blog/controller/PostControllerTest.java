package pl.example.simple_blog.controller;

import org.aspectj.lang.annotation.Before;
import org.json.JSONException;
import org.json.JSONObject;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SimpleBlogApplication.class)
class PostControllerTest {

    @LocalServerPort
    public int randomServerPort;


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
        }); // generic types with Spring RestTemplate we need to use ParameterizedTypeReference

        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // content verification
        List<Post> post = responseEntity.getBody();

        Assertions.assertEquals(1, post.size());
        Post theOnlyElement = post.get(0);

        Assertions.assertEquals("First title", theOnlyElement.getTitle());
        Assertions.assertEquals("The body of content", theOnlyElement.getContent());
        Assertions.assertNotNull(theOnlyElement.getCreationDate());
        Assertions.assertNull(theOnlyElement.getUpdateDate());
    }

    @Test
    public void test_deleteAddedObjectDoesNotThrowErrorsAndReturnsStatusCodeOK() throws JSONException {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        // adding Entity
        // JSON body to added entity
        JSONObject addBody = new JSONObject();
        addBody.put("title", "added title");
        addBody.put("content", "added new contend");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(addBody.toString(), headers);

        // add Entity
        ResponseEntity<Void> addResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + randomServerPort + "/api/post/add",
                HttpMethod.POST, request, Void.class);
        // status code verification
        Assertions.assertEquals(HttpStatus.CREATED, addResponseEntity.getStatusCode());
        // DB state verification
        ResponseEntity<List<Post>> responseEntity = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/post", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Post>>() {
        });
        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // deleting entity
        ResponseEntity<List<Post>> deleteResponseEntity = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/post/1", HttpMethod.DELETE, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Post>>() {
        });
        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // content verification
        List<Post> post = deleteResponseEntity.getBody();
        Assertions.assertNull(post);

    }

    @Test
    public void test_updateAddedObjectDoesNotThrowErrorsAndReturnsStatusCodeACCEPTED() {
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
        }); // generic types with Spring RestTemplate we need to use ParameterizedTypeReference

        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<Post> postList = responseEntity.getBody();
        Post post = postList.get(0);
        Long id = post.getId();

        ResponseEntity<List<Post>> updateResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + randomServerPort + "/api/post/update/" + id.toString(),
                HttpMethod.PATCH,
                new HttpEntity<>(
                        Post.builder()
                                .title("Updated title")
                                .content("Updated the body of content")
                                .build()
                ),
                new ParameterizedTypeReference<List<Post>>() {
                }); // generic types with Spring RestTemplate we need to use ParameterizedTypeReference;
        // status code verification
        Assertions.assertEquals(HttpStatus.ACCEPTED, updateResponseEntity.getStatusCode());
        ResponseEntity<List<Post>> updatedResponseEntity = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/post", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Post>>() {
        });
        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // content verification
        List<Post> updatedPostList = updatedResponseEntity.getBody();
        System.out.println(updatedPostList);
        Post updatedPost = updatedPostList.get(0);
        Assertions.assertEquals("Updated title", updatedPost.getTitle());
        Assertions.assertEquals("Updated the body of content", updatedPost.getContent());

    }

}