package com.fake.api.jsonplaceholder.service;

import com.fake.api.jsonplaceholder.exception.NotFoundException;
import com.fake.api.jsonplaceholder.model.Comment;
import com.fake.api.jsonplaceholder.model.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class PostsService {
    @Value("${base.url}")
    private String BASE_URL;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public ResponseEntity<List<Post>> getPosts(Long userId, String title) {
        String userIdParam = userId != null ? "userId=" + userId : "";
        String titleParam = title != null ? "title=" + title : "";
        // Get the response from the client
        Mono<ResponseEntity<Post[]>> responseClient;

        if (!userIdParam.isEmpty() && !titleParam.isEmpty()) {
            responseClient = callWebClientGet("/posts?" + userIdParam + "&" + titleParam, Post[].class);
        } else if (!userIdParam.isEmpty()) {
            responseClient = callWebClientGet("/posts?" + userIdParam, Post[].class);
        } else if (!titleParam.isEmpty()) {
            responseClient = callWebClientGet("/posts?" + titleParam, Post[].class);
        } else {
            responseClient = callWebClientGet("/posts", Post[].class);
        }

        ResponseEntity<Post[]> responseEntity = responseClient.block();

        return new ResponseEntity<>(List.of(responseEntity.getBody()), responseEntity.getStatusCode());
    }

    public ResponseEntity<Post> getPost(String id){
        // Get the response from the client
        Mono<ResponseEntity<Post>> responseClient = callWebClientGet("/posts/" + id, Post.class);

        ResponseEntity<Post> responseEntity = responseClient.block();

        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    public ResponseEntity<List<Post>> fetchAndSaveData(Long userId, String title) {
        ResponseEntity<List<Post>> posts = getPosts(userId, title);
        List<Post> postsList = posts.getBody();

        // Save as JSON
         ObjectMapper jsonMapper = new ObjectMapper();
         try {
             jsonMapper.writeValue(new File("posts.json"), postsList);
         } catch (IOException e) {
             throw new RuntimeException("Error saving posts as JSON", e);
         }

        // Save as XML
         XmlMapper xmlMapper = new XmlMapper();
         try {
             xmlMapper.writeValue(new File("posts.xml"), postsList);
         } catch (IOException e) {
             throw new RuntimeException("Error saving posts as XML", e);
         }

        return posts;
    }

    public ResponseEntity<List<Comment>> getComments(String id, Long commentId){
        String commentIdParam = commentId != null ? "id=" + commentId : "";
        // Get the response from the client
        Mono<ResponseEntity<Comment[]>> responseClient;

        if (!commentIdParam.isEmpty()) {
            responseClient = callWebClientGet("/posts/" + id + "/comments?" + commentIdParam, Comment[].class);
        } else {
            responseClient = callWebClientGet("/posts/" + id + "/comments", Comment[].class);
        }

        ResponseEntity<Comment[]> responseEntity = responseClient.block();

        return new ResponseEntity<>(List.of(responseEntity.getBody()), responseEntity.getStatusCode());
    }

    public ResponseEntity<Post> createPost(Post post){
        // Get the response from the client
        Mono<ResponseEntity<Post>> responseClient = callWebClientPost("/posts/", post, Post.class);

        ResponseEntity<Post> responseEntity = responseClient.block();

        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    public ResponseEntity<Post> updatePost(String id, Post post){
        // Get the response from the client
        Mono<ResponseEntity<Post>> responseClient = callWebClientPut("/posts/" + id, post, Post.class);

        ResponseEntity<Post> responseEntity = responseClient.block();

        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    public ResponseEntity<Post> patchPost(String id, Post post){
        // Get the original post
        ResponseEntity<Post> originalPostResponse = getPost(id);
        Post originalPost = originalPostResponse.getBody();

        // Get the response from the client
        Mono<ResponseEntity<Post>> responseClient = callWebClientPatch("/posts/" + id, post, Post.class);

        ResponseEntity<Post> responseEntity = responseClient.block();

        Post updatedPost = combinePosts(originalPost, responseEntity.getBody());

        return new ResponseEntity<>(updatedPost, responseEntity.getStatusCode());
    }

    public ResponseEntity<Void> deletePost(String id) {
        Mono<ResponseEntity<Void>> responseClient = callWebClientDelete("/posts/" + id, Void.class);

        ResponseEntity<Void> responseEntity = responseClient.block();

        return new ResponseEntity<>(responseEntity.getStatusCode());
    }

    private <T> Mono<ResponseEntity<T>> callWebClientGet(String uri, Class<T> responseType) {
        WebClient webClient = webClientBuilder.baseUrl(BASE_URL).build();
        return webClient.get()
                .uri(uri)
                .exchangeToMono(response -> handleResponse(response, responseType));
    }

    private <T> Mono<ResponseEntity<T>> callWebClientPost (String uri, Post body, Class<T> responseType){
        WebClient webClient = webClientBuilder.baseUrl(BASE_URL).build();
        return webClient.post()
                .uri(uri)
                .body(Mono.just(body), responseType)
                .exchangeToMono(response -> handleResponse(response, responseType));
    }

    private <T> Mono<ResponseEntity<T>> callWebClientPut (String uri, Post body, Class<T> responseType){
        WebClient webClient = webClientBuilder.baseUrl(BASE_URL).build();
        return webClient.put()
                .uri(uri)
                .body(Mono.just(body), responseType)
                .exchangeToMono(response -> handleResponse(response, responseType));
    }

    private <T> Mono<ResponseEntity<T>> callWebClientPatch (String uri, Post body, Class<T> responseType){
        WebClient webClient = webClientBuilder.baseUrl(BASE_URL).build();
        return webClient.patch()
                .uri(uri)
                .body(Mono.just(body), responseType)
                .exchangeToMono(response -> handleResponse(response, responseType));
    }

    private Mono<ResponseEntity<Void>> callWebClientDelete(String uri, Class<Void> responseType) {
        WebClient webClient = webClientBuilder.baseUrl(BASE_URL).build();
        return webClient.delete()
                .uri(uri)
                .exchangeToMono(response -> handleResponse(response, responseType));
    }

    private <T> Mono<ResponseEntity<T>> handleResponse(ClientResponse response, Class<T> responseType) {
        if (response.statusCode().is2xxSuccessful()) {
            if (responseType == Void.class) {
                return Mono.just(new ResponseEntity<>(response.statusCode()));
            } else {
                return response.bodyToMono(responseType).map(body -> new ResponseEntity<>(body, response.statusCode()));
            }
        }
        else if (response.statusCode().is4xxClientError()) {
            // Handle client errors (e.g., 404 Not Found)
            if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new NotFoundException("Post not found");
            }
            throw new RuntimeException("Client error");
        }
        else if (response.statusCode().is5xxServerError()) {
            // Handle server errors (e.g., 500 Internal Server Error)
            throw new RuntimeException("Server error");
        }
        else {
            // Handle other status codes as needed
            throw new RuntimeException("Unexpected error");
        }
    }

    private Post combinePosts(Post originalPost, Post updatedPost) {
        if (updatedPost.getUserId() != null) {
            originalPost.setUserId(updatedPost.getUserId());
        }
        if (updatedPost.getTitle() != null) {
            originalPost.setTitle(updatedPost.getTitle());
        }
        if (updatedPost.getBody() != null) {
            originalPost.setBody(updatedPost.getBody());
        }
        return originalPost;
    }

}
