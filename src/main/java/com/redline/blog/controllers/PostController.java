package com.redline.blog.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redline.blog.domain.CreatePostRequest;
import com.redline.blog.domain.UpdatePostRequest;
import com.redline.blog.domain.dto.CreatePostRequestDto;
import com.redline.blog.domain.dto.PostDto;
import com.redline.blog.domain.dto.UpdatePostRequestDto;
import com.redline.blog.domain.entities.Post;
import com.redline.blog.domain.entities.User;
import com.redline.blog.mappers.PostMapper;
import com.redline.blog.services.PostService;
import com.redline.blog.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId) {

        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        return ResponseEntity.ok(posts.stream().map(postMapper::toDto).toList());
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId) {
        User loggedInUser = userService.getUserById(userId);
        return ResponseEntity.ok(
                postService.getDraftPosts(loggedInUser)
                        .stream()
                        .map(postMapper::toDto)
                        .toList());
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId) {

        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post createdPost = postService.createPost(loggedInUser, createPostRequest);
        return new ResponseEntity<>(postMapper.toDto(createdPost), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable("id") UUID postId,
            @RequestBody UpdatePostRequestDto updatePostRequestDto) {

        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);

        return ResponseEntity.ok(postMapper.toDto(
                postService.updatePost(postId, updatePostRequest)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable("id") UUID postId) {
        postService.getPost(postId);
        PostDto post = postMapper.toDto(postService.getPost(postId));
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") UUID postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
