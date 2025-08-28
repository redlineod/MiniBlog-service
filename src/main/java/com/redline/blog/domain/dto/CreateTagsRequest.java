package com.redline.blog.domain.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTagsRequest {

    @NotEmpty(message = "At least one tag name must be provided")
    @Size(max = 10, message = "A maximum of {max} tags can be created at once")

    private Set<@Size(min = 2, max = 15, message = "Tag name must be between {min} and {max} characters")
            @NotBlank(message = "Tag name must not be blank")
            @Pattern(regexp = "^[\\w\\s-]+$", message = "Tag name can only contain letters, numbers, spaces, hyphens, and underscores") String> names;
}
