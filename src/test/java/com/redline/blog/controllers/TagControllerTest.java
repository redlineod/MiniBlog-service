package com.redline.blog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redline.blog.domain.dto.CreateTagsRequest;
import com.redline.blog.domain.dto.TagDto;
import com.redline.blog.domain.entities.Tag;
import com.redline.blog.mappers.TagMapper;
import com.redline.blog.services.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TagService tagService;

    @MockitoBean
    private TagMapper tagMapper;

    @Test
    void getAllTags_shouldReturnListOfTagDtos() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Tag tag1 = Tag.builder().id(id1).name("java").build();
        Tag tag2 = Tag.builder().id(id2).name("spring").build();

        TagDto dto1 = TagDto.builder().id(id1).name("java").postCount(3).build();
        TagDto dto2 = TagDto.builder().id(id2).name("spring").postCount(5).build();

        when(tagService.getTags()).thenReturn(List.of(tag1, tag2));
        when(tagMapper.toTagResponse(tag1)).thenReturn(dto1);
        when(tagMapper.toTagResponse(tag2)).thenReturn(dto2);

        mockMvc.perform(get("/api/v1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(id1.toString()))
                .andExpect(jsonPath("$[0].name").value("java"))
                .andExpect(jsonPath("$[0].postCount").value(3))
                .andExpect(jsonPath("$[1].id").value(id2.toString()))
                .andExpect(jsonPath("$[1].name").value("spring"))
                .andExpect(jsonPath("$[1].postCount").value(5));

        verify(tagService).getTags();
        verify(tagMapper).toTagResponse(tag1);
        verify(tagMapper).toTagResponse(tag2);
    }

    @Test
    void saveTags_shouldCreateAndReturnTags() throws Exception {
        Set<String> names = Set.of("java", "spring");
        CreateTagsRequest request = CreateTagsRequest.builder().names(names).build();

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Tag tag1 = Tag.builder().id(id1).name("java").build();
        Tag tag2 = Tag.builder().id(id2).name("spring").build();

        TagDto dto1 = TagDto.builder().id(id1).name("java").postCount(0).build();
        TagDto dto2 = TagDto.builder().id(id2).name("spring").postCount(0).build();

        when(tagService.createTags(names)).thenReturn(List.of(tag1, tag2));
        when(tagMapper.toTagResponse(tag1)).thenReturn(dto1);
        when(tagMapper.toTagResponse(tag2)).thenReturn(dto2);

        mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("java"))
                .andExpect(jsonPath("$[1].name").value("spring"));

        verify(tagService).createTags(names);
        verify(tagMapper).toTagResponse(tag1);
        verify(tagMapper).toTagResponse(tag2);
    }

    @Test
    void deleteTag_shouldReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/tags/{id}", id))
                .andExpect(status().isNoContent());

        verify(tagService).deleteTagById(id);
    }
}