package com.redline.blog.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {
    private int status;
    private String message;
    private List<FieldError> errors;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldError {
        String field;
        String message;
    }
}
