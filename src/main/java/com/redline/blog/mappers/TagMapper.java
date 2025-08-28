package com.redline.blog.mappers;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.redline.blog.domain.PostStatus;
import com.redline.blog.domain.dto.CreateTagsRequest;
import com.redline.blog.domain.dto.TagDto;
import com.redline.blog.domain.entities.Post;
import com.redline.blog.domain.entities.Tag;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagDto toTagResponse(Tag tag);
    
    Tag toEntity(CreateTagsRequest tag);

    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Post> posts) {
        if (posts == null) {
            return 0;
        }
        return (int) posts.stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED)).count();
    }
}
