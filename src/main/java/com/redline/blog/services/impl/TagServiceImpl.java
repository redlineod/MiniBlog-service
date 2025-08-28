package com.redline.blog.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.redline.blog.domain.entities.Tag;
import com.redline.blog.repositories.TagRepository;
import com.redline.blog.services.TagService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostsCount();
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);
        Set<String> existingTagNames = existingTags.stream().map(Tag::getName).collect(Collectors.toSet());

        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build())
                .collect(Collectors.toList());

        List<Tag> savedTags = new ArrayList<>();
        if (!newTags.isEmpty()) {
            savedTags = tagRepository.saveAll(newTags);
        }
        savedTags.addAll(existingTags);
        return savedTags;
    }

    @Transactional
    @Override
    public void deleteTagById(UUID tagId) {
        Optional<Tag> existingTag = tagRepository.findById(tagId);
        if (existingTag.isEmpty()) {
            throw new IllegalArgumentException("Tag with the given ID does not exist");
        }
        if (!existingTag.get().getPosts().isEmpty()) {
            throw new IllegalStateException("Cannot delete tag with associated posts");
        }
        tagRepository.deleteById(tagId);
    }

    @Override
    public Tag getTagById(UUID tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + tagId));
    }

    @Override
    public List<Tag> getTagsByIds(Set<UUID> tagIds) {
        List<Tag> tags = tagRepository.findAllById(tagIds);

        if (tags.size() != tagIds.size()) {
            throw new EntityNotFoundException("One or more tags not found for the provided IDs");
        }

        return tags;
    }

}
