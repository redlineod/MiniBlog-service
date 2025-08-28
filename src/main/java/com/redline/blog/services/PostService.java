package com.redline.blog.services;

import java.util.List;
import java.util.UUID;

import com.redline.blog.domain.CreatePostRequest;
import com.redline.blog.domain.UpdatePostRequest;
import com.redline.blog.domain.entities.Post;
import com.redline.blog.domain.entities.User;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID postId, UpdatePostRequest updatePostRequest);
    Post getPost(UUID postId);
    void deletePost(UUID postId);
}
