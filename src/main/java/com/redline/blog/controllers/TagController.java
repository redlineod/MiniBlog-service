package com.redline.blog.controllers;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redline.blog.domain.dto.CreateTagsRequest;
import com.redline.blog.domain.dto.TagDto;
import com.redline.blog.domain.entities.Tag;
import com.redline.blog.mappers.TagMapper;
import com.redline.blog.services.TagService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping()
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<Tag> tags = tagService.getTags();
        List<TagDto> tagResponses = tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(tagResponses);
    }

    @PostMapping()
    public ResponseEntity<List<TagDto>> saveTags(@Valid @RequestBody CreateTagsRequest createTagsRequest) {
        List<Tag> savedTags = tagService.createTags(createTagsRequest.getNames());
        List<TagDto> createdTagResponses = savedTags.stream().map(tagMapper::toTagResponse).toList();

        return new ResponseEntity<>(createdTagResponses, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") UUID tagId) {
        tagService.deleteTagById(tagId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
