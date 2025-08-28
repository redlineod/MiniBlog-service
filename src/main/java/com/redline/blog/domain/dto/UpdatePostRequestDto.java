package com.redline.blog.domain.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.redline.blog.domain.PostStatus;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePostRequestDto {
    
    @Nonnull
    private UUID id;
    
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between {min} and {max} characters")
    private String title;
    
    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 50000, message = "Content must be between {min} and {max} characters")
    private String content;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @Builder.Default
    @Size(max = 10, message = "A maximum of {max} tags are allowed")
    private Set<UUID> tagIds = new HashSet<>();

    @NotNull(message = "Status is required")
    private PostStatus status;
}
