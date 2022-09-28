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
import pl.example.simple_blog.model.PostDTO;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SimpleBlogApplication.class)
class PostControllerTest {

    @LocalServerPort
    public int randomServerPort;


    @Test
    public void test_emptyListAfterStartDoesNotThrowErrorsAndReturnsStatusCodeOK() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();

        ResponseEntity<List<PostDTO>> responseEntity = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/post", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<PostDTO>>() {
        });

        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // content verification
        List<PostDTO> postList = responseEntity.getBody();
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
        ResponseEntity<List<PostDTO>> responseEntity = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/post", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<PostDTO>>() {
        }); // generic types with Spring RestTemplate we need to use ParameterizedTypeReference

        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // content verification
        List<PostDTO> postDTO = responseEntity.getBody();

        Assertions.assertEquals(1, postDTO.size());
        PostDTO theOnlyElement = postDTO.get(0);

        Assertions.assertEquals("First title", theOnlyElement.getPostTitle() );
        Assertions.assertEquals("The body of content", theOnlyElement.getContentBody());
        Assertions.assertNotNull(theOnlyElement.getCreationDate());

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
        ResponseEntity<List<PostDTO>> responseEntity = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/post", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<PostDTO>>() {
        }); // generic types with Spring RestTemplate we need to use ParameterizedTypeReference

        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<PostDTO> postList = responseEntity.getBody();
        PostDTO post = postList.get(0);
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
        ResponseEntity<List<PostDTO>> updatedResponseEntity = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/post", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<PostDTO>>() {
        });
        // status code verification
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // content verification
        List<PostDTO> updatedPostList = updatedResponseEntity.getBody();
        System.out.println(updatedPostList);
        PostDTO updatedPost = updatedPostList.get(0);
        Assertions.assertEquals("Updated title", updatedPost.getPostTitle());
        Assertions.assertEquals("Updated the body of content", updatedPost.getContentBody());

    }

}