package com.redline.blog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redline.blog.domain.PostStatus;
import com.redline.blog.domain.dto.CreateTagsRequest;
import com.redline.blog.domain.entities.Category;
import com.redline.blog.domain.entities.Post;
import com.redline.blog.domain.entities.Tag;
import com.redline.blog.domain.entities.User;
import com.redline.blog.repositories.CategoryRepository;
import com.redline.blog.repositories.PostRepository;
import com.redline.blog.repositories.TagRepository;
import com.redline.blog.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class TagControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TagController tagController;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void getAllTags_whenEmpty_shouldReturnEmptyList() {
        assertNotNull(tagController);
        assertNotNull(tagRepository);
        assertNotNull(tagController.getAllTags());
        assertNotNull(tagController.getAllTags().getBody());
        assertTrue(tagController.getAllTags().getBody().isEmpty());
    }

    @Test
    void getAllTags_whenNotEmpty_shouldReturnListOfTagDtos() {
        // Set up test data
        Tag tag1 = Tag.builder()
                .name("java")
                .build();
        Tag tag2 = Tag.builder()
                .name("spring")
                .build();

        tagRepository.save(tag1);
        tagRepository.save(tag2);

        // Single call to avoid multiple HTTP requests in an integration test
        var response = tagController.getAllTags();
        var tagDtos = response.getBody();

        assertNotNull(tagController);
        assertNotNull(response);
        assertNotNull(tagDtos);
        assertFalse(tagDtos.isEmpty());
        assertEquals(2, tagDtos.size());
        assertNotNull(tagDtos.getFirst());
        assertNotNull(tagDtos.getFirst().getId());
    }

    @Test
    void saveTags_whenValid_shouldCreateAndReturnCreatedList() {
        // When
        var response = tagController.saveTags(CreateTagsRequest.builder()
                .names(Set.of("java", "spring"))
                .build());

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        List<String> names = response.getBody().stream().map(dto -> dto.getName()).toList();
        assertTrue(names.containsAll(Set.of("java", "spring")));
    }

    @Test
    void saveTags_whenSomeExist_shouldNotDuplicateAndReturnAll() {
        // Given existing tag
        tagRepository.save(Tag.builder().name("java").build());

        // When creating with an existing and a new one
        var response = tagController.saveTags(CreateTagsRequest.builder()
                .names(Set.of("java", "spring"))
                .build());

        // Then
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        List<String> names = response.getBody().stream().map(dto -> dto.getName()).toList();
        assertTrue(names.containsAll(Set.of("java", "spring")));

        // Ensure repository has only 2 entries
        assertEquals(2, tagRepository.findAll().size());
    }

    @Test
    void deleteTag_whenExistsAndNoPosts_shouldReturnNoContent() {
        // Given
        Tag tag = tagRepository.save(Tag.builder().name("to-delete").build());

        // When
        var response = tagController.deleteTag(tag.getId());

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(tagRepository.findById(tag.getId()).isPresent());
    }

    @Test
    void testSaveTags_nullNames_shouldThrowMethodArgumentNotValidException() throws Exception {
        CreateTagsRequest request = CreateTagsRequest.builder().names(null).build();

        mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void deleteTag_whenHasAssociatedPosts_shouldThrowIllegalStateException() {
        // Given: create tag, user, category, post and link
        Tag tag = tagRepository.save(Tag.builder().name("linked").build());
        User user = userRepository.save(User.builder()
                .email("user@example.com")
                .password("password")
                .name("User")
                .build());
        Category category = categoryRepository.save(Category.builder().name("Tech").build());
        Post post = Post.builder()
                .title("Title")
                .content("Content")
                .status(PostStatus.PUBLISHED)
                .readingTime(5)
                .author(user)
                .category(category)
                .build();
        post.getTags().add(tag);
        // manage the inverse side for consistency (optional for DB relation, but keeps in-memory consistent)
        tag.getPosts().add(post);
        postRepository.save(post);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> tagController.deleteTag(tag.getId()));
        assertEquals("Cannot delete tag with associated posts", exception.getMessage());
    }

    @Test
    void deleteTag_whenTagNotFound_shouldThrowIllegalArgumentException() {
        // Given - non-existent tag ID
        UUID nonExistentTagId = UUID.randomUUID();

        // When & Then - verify that the exception is thrown
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> tagController.deleteTag(nonExistentTagId)
        );

        // Verify the exception message
        assertEquals("Tag with the given ID does not exist", exception.getMessage());
    }
}