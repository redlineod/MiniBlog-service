package com.redline.blog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.redline.blog.domain.CreatePostRequest;
import com.redline.blog.domain.UpdatePostRequest;
import com.redline.blog.domain.dto.CreatePostRequestDto;
import com.redline.blog.domain.dto.PostDto;
import com.redline.blog.domain.dto.UpdatePostRequestDto;
import com.redline.blog.domain.entities.Post;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto createPostRequestDto);
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto updatePostRequestDto);

}
