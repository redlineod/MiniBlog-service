package com.redline.blog.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redline.blog.domain.CreatePostRequest;
import com.redline.blog.domain.PostStatus;
import com.redline.blog.domain.UpdatePostRequest;
import com.redline.blog.domain.entities.Category;
import com.redline.blog.domain.entities.Post;
import com.redline.blog.domain.entities.Tag;
import com.redline.blog.domain.entities.User;
import com.redline.blog.repositories.PostRepository;
import com.redline.blog.services.CategoryService;
import com.redline.blog.services.PostService;
import com.redline.blog.services.TagService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final int AVERAGE_READING_SPEED_WPM = 200;

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED, category, tag);
        }

        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        }

        if (tagId != null) {
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED, tag);
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByStatusAndAuthor(PostStatus.DRAFT, user);
    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = Post.builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .status(createPostRequest.getStatus())
                .author(user)
                .build();
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagsByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UUID postId, UpdatePostRequest updatePostRequest) {
        Post postToUpdate = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
        postToUpdate.setTitle(updatePostRequest.getTitle());
        postToUpdate.setContent(updatePostRequest.getContent());
        postToUpdate.setStatus(updatePostRequest.getStatus());
        postToUpdate.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        if (!postToUpdate.getCategory().getId().equals(updatePostRequest.getCategoryId())) {
            Category category = categoryService.getCategoryById(updatePostRequest.getCategoryId());
            postToUpdate.setCategory(category);
        }

        Set<UUID> updatedTagIds = updatePostRequest.getTagIds();
        Set<UUID> existingTagIds = postToUpdate.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        if (!existingTagIds.equals(updatedTagIds)) {
            List<Tag> tags = tagService.getTagsByIds(updatedTagIds);
            postToUpdate.setTags(new HashSet<>(tags));
        }

        return postRepository.save(postToUpdate);
    }

    @Override
    @Transactional
    public void deletePost(UUID postId) {
        postRepository.deleteById(postId);
    }

    private int calculateReadingTime(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }

        String[] words = content.trim().split("\\s+");
        int wordCount = words.length;
        return Math.max(1, wordCount / AVERAGE_READING_SPEED_WPM);
    }

    @Override
    public Post getPost(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
    }

}
