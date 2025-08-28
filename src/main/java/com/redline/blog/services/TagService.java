package com.redline.blog.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.redline.blog.domain.entities.Tag;

public interface TagService {
    List<Tag> getTags();
    List<Tag> createTags(Set<String> tagNames);
    void deleteTagById(UUID tagId);
    Tag getTagById(UUID tagId);
    List<Tag> getTagsByIds(Set<UUID> tagIds);
}
