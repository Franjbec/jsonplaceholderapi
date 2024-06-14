package com.fake.api.jsonplaceholder.controller;

import com.fake.api.jsonplaceholder.model.Comment;
import com.fake.api.jsonplaceholder.model.Post;
import com.fake.api.jsonplaceholder.service.PostsService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/")
@Slf4j
@Tag(name = "Post Management System", description = "Operations pertaining to posts in the Post Management System")
public class PostsController {

    @Autowired
    private PostsService service;

    @Operation(summary = "Retrieve posts from the API. You can filter by userId or title.")
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "title", required = false) String title) {
        return service.getPosts(userId, title);
    }

    @Operation(summary = "Retrieve posts from the API by Id.")
    @GetMapping("posts/{id}")
    public ResponseEntity<Post> getPost(
            @Parameter(description = "ID of the post", required = true, schema = @Schema(type = "integer"))
            @PathVariable String id){
        return service.getPost(id);
    }

    @Operation(summary = "Fetch data from API and save it as JSON and XML. You can filter by userId or title.")
    @GetMapping("/fetch-and-save")
    public ResponseEntity<List<Post>> fetchAndSaveData(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "title", required = false) String title) {
        return service.fetchAndSaveData(userId, title);
    }

    @Operation(summary = "Retrieve comments from the API related to Posts. You can filter by commentId.")
    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(
            @PathVariable String id,
            @RequestParam(value = "commentId", required = false) Long commentId){
        return service.getComments(id, commentId);
    }

    @Operation(summary = "Create a new post")
    @PostMapping("posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post){
        return service.createPost(post);
    }

    @Operation(summary = "Update a post by Id")
    @PutMapping("posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable String id, @RequestBody Post post){
        return service.updatePost(id, post);
    }

    @Operation(summary = "Patch a post by Id")
    @PatchMapping("posts/{id}")
    public ResponseEntity<Post> patchPost(@PathVariable String id, @RequestBody Post post){
        return service.patchPost(id, post);
    }

    @Operation(summary = "Delete a post by Id")
    @DeleteMapping("posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id){
        return service.deletePost(id);
    }
}
