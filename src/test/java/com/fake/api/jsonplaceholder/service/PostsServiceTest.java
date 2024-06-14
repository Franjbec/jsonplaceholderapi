package com.fake.api.jsonplaceholder.service;

import com.fake.api.jsonplaceholder.exception.NotFoundException;
import com.fake.api.jsonplaceholder.model.Comment;
import com.fake.api.jsonplaceholder.model.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import okhttp3.mockwebserver.MockWebServer;

public class PostsServiceTest {

    private MockWebServer mockWebServer;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private PostsService postsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        MockitoAnnotations.openMocks(this); // Inicializa los mocks

        // Configura el WebClient.Builder para apuntar al MockWebServer
        WebClient webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();
        when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testGetPosts() throws Exception {
        // Mock response body
        ObjectMapper objectMapper = new ObjectMapper();
        Post mockPost = new Post(1L, 1L, "Title", "Body");
        String responseBody = objectMapper.writeValueAsString(new Post[]{mockPost});

        // Enqueue a mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json"));

        // Configure WebClient mock to use MockWebServer URL
        WebClient webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();
        when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        // Call service method to get posts
        ResponseEntity<List<Post>> actualResponseEntity = postsService.getPosts(null, null);

        // Assertions
        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        assertEquals(1, actualResponseEntity.getBody().size());
        assertEquals(mockPost.getId(), actualResponseEntity.getBody().get(0).getId());
        assertEquals(mockPost.getUserId(), actualResponseEntity.getBody().get(0).getUserId());
        assertEquals(mockPost.getTitle(), actualResponseEntity.getBody().get(0).getTitle());
        assertEquals(mockPost.getBody(), actualResponseEntity.getBody().get(0).getBody());
    }

    @Test
    public void testGetPostById() throws Exception {
        // Mock response body
        Post mockPost = new Post(1L, 1L, "Title", "Body");
        String responseBody = objectMapper.writeValueAsString(mockPost);

        // Enqueue a mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json"));

        // Call service method to get post
        ResponseEntity<Post> actualResponseEntity = postsService.getPost("1");

        // Assertions
        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        assertEquals(mockPost.getId(), actualResponseEntity.getBody().getId());
        assertEquals(mockPost.getUserId(), actualResponseEntity.getBody().getUserId());
        assertEquals(mockPost.getTitle(), actualResponseEntity.getBody().getTitle());
        assertEquals(mockPost.getBody(), actualResponseEntity.getBody().getBody());
    }

    @Test
    public void testGetPostById_NotFound() {
        // Enqueue a mock response 404 Not Found
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
        );

        // Call service method to get a post by ID that does not exist
        try {
            postsService.getPost("999");
        } catch (NotFoundException e) {
            assertEquals("Post not found", e.getMessage());
        }
    }


    @Test
    public void testFetchAndSaveData() throws Exception {
        // Mock response body
        Post mockPost = new Post(1L, 1L, "Title", "Body");
        String responseBody = objectMapper.writeValueAsString(new Post[]{mockPost});

        // Enqueue a mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json"));

        // Call service method to fetch and save data
        ResponseEntity<List<Post>> actualResponseEntity = postsService.fetchAndSaveData(null, null);

        // Assertions
        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        assertEquals(1, actualResponseEntity.getBody().size());
        assertEquals(mockPost.getId(), actualResponseEntity.getBody().get(0).getId());
        assertEquals(mockPost.getUserId(), actualResponseEntity.getBody().get(0).getUserId());
        assertEquals(mockPost.getTitle(), actualResponseEntity.getBody().get(0).getTitle());
        assertEquals(mockPost.getBody(), actualResponseEntity.getBody().get(0).getBody());

        // Verify files are saved correctly
        File jsonFile = new File("posts.json");
        File xmlFile = new File("posts.xml");
        assertTrue(jsonFile.exists());
        assertTrue(xmlFile.exists());
    }

    @Test
    public void testGetComments() throws Exception {
        // Mock response body
        Comment mockComment = new Comment(1L, 1L, "Name", "Email", "Body");
        String responseBody = objectMapper.writeValueAsString(new Comment[]{mockComment});

        // Enqueue a mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json"));

        // Call service method to get comments
        ResponseEntity<List<Comment>> actualResponseEntity = postsService.getComments("1", null);

        // Assertions
        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        assertEquals(1, actualResponseEntity.getBody().size());
        assertEquals(mockComment.getId(), actualResponseEntity.getBody().get(0).getId());
        assertEquals(mockComment.getPostId(), actualResponseEntity.getBody().get(0).getPostId());
        assertEquals(mockComment.getName(), actualResponseEntity.getBody().get(0).getName());
        assertEquals(mockComment.getEmail(), actualResponseEntity.getBody().get(0).getEmail());
        assertEquals(mockComment.getBody(), actualResponseEntity.getBody().get(0).getBody());
    }

    @Test
    public void testCreatePost() throws Exception {
        // Mock request body
        Post mockPost = new Post(null, 1L, "Title", "Body");
        String requestBody = objectMapper.writeValueAsString(mockPost);

        // Mock response body
        String responseBody = objectMapper.writeValueAsString(mockPost);

        // Enqueue a mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json"));

        // Call service method to create post
        ResponseEntity<Post> actualResponseEntity = postsService.createPost(mockPost);

        // Assertions
        assertEquals(HttpStatus.CREATED, actualResponseEntity.getStatusCode());
        assertEquals(mockPost.getUserId(), actualResponseEntity.getBody().getUserId());
        assertEquals(mockPost.getTitle(), actualResponseEntity.getBody().getTitle());
        assertEquals(mockPost.getBody(), actualResponseEntity.getBody().getBody());
    }

    @Test
    public void testUpdatePost() throws Exception {
        // Mock request body
        Post mockPost = new Post(null, 1L, "Updated Title", "Updated Body");
        String requestBody = objectMapper.writeValueAsString(mockPost);

        // Mock response body
        String responseBody = objectMapper.writeValueAsString(mockPost);

        // Enqueue a mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json"));

        // Call service method to update post
        ResponseEntity<Post> actualResponseEntity = postsService.updatePost("1", mockPost);

        // Assertions
        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        assertEquals(mockPost.getUserId(), actualResponseEntity.getBody().getUserId());
        assertEquals(mockPost.getTitle(), actualResponseEntity.getBody().getTitle());
        assertEquals(mockPost.getBody(), actualResponseEntity.getBody().getBody());
    }

    @Test
    public void testPatchPost() throws IOException {
        // Mock original post
        Post mockOriginalPost = new Post(1L, 1L, "Original Title", "Original Body");

        // Mock updated post (partial update)
        Post mockUpdatedPost = new Post(null, null, "Updated Title", null);

        // Mock response body for original and updated posts
        String originalPostResponseBody = objectMapper.writeValueAsString(mockOriginalPost);
        String updatedPostResponseBody = objectMapper.writeValueAsString(mockUpdatedPost);

        // Enqueue mock response for original post
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(originalPostResponseBody)
                .addHeader("Content-Type", "application/json"));

        // Enqueue mock response for updated post
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(updatedPostResponseBody)
                .addHeader("Content-Type", "application/json"));

        // Configure WebClient mock to use MockWebServer URL
        WebClient webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();
        when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        // Call service method to patch post
        ResponseEntity<Post> actualResponseEntity = postsService.patchPost("1", mockUpdatedPost);

        // Assertions
        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        assertEquals(mockOriginalPost.getId(), actualResponseEntity.getBody().getId());
        assertEquals(mockOriginalPost.getUserId(), actualResponseEntity.getBody().getUserId());
        assertEquals(mockUpdatedPost.getTitle(), actualResponseEntity.getBody().getTitle());
        assertEquals(mockOriginalPost.getBody(), actualResponseEntity.getBody().getBody());
    }

    @Test
    public void testDeletePost() {
        // Mock response body
        String responseBody = ""; // Assuming empty response body for delete operation

        // Enqueue a mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(204) // No content
                .setBody(responseBody));

        // Call service method to delete post
        ResponseEntity<Void> actualResponseEntity = postsService.deletePost("1");

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, actualResponseEntity.getStatusCode());
        assertTrue(actualResponseEntity.getBody() == null || actualResponseEntity.getBody().toString().isEmpty());
    }

    // Helper method to verify client error handling
    @Test
    public void testServerErrorHandling() {
        // Mock response for WebClient with 500 error
        // Enqueue a mock response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
        );

        // Call service method to get a post by ID
        try {
            postsService.getPost("1");
        } catch (RuntimeException e) {
            assertEquals("Server error", e.getMessage());
        }
    }

}