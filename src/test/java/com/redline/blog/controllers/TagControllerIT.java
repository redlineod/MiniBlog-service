package com.redline.blog.controllers;

import com.redline.blog.domain.entities.Tag;
import com.redline.blog.repositories.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TagControllerIT {

    @Autowired
    TagController tagController;

    @Autowired
    TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository.deleteAll();
    }

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
}