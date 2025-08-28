package com.redline.blog.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redline.blog.domain.dto.CategoryDto;
import com.redline.blog.domain.dto.CreateCategoryRequest;
import com.redline.blog.domain.entities.Category;
import com.redline.blog.mappers.CategoryMapper;
import com.redline.blog.services.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> listCategories() {
        List<CategoryDto> categories = categoryService.listCategories()
                .stream().map(categoryMapper::toDto).toList();

        return ResponseEntity.ok(categories);
    }

    @PostMapping()
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        Category category = categoryMapper.toEntity(createCategoryRequest);

        Category savedCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(
                categoryMapper.toDto(savedCategory),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") UUID categoryId)
    {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
    }
}
