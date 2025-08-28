package com.redline.blog.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.redline.blog.domain.PostStatus;
import com.redline.blog.domain.dto.CategoryDto;
import com.redline.blog.domain.dto.CreateCategoryRequest;
import com.redline.blog.domain.entities.Category;
import com.redline.blog.domain.entities.Post;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostsCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("calculatePostsCount")
    default Long calculatePostsCount(List<Post> posts) {
        if (posts == null) {
            return 0L;
        }
        return posts.stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED)).count();
    }
}
