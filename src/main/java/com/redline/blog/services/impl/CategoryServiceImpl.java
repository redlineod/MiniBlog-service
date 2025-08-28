package com.redline.blog.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.redline.blog.domain.entities.Category;
import com.redline.blog.repositories.CategoryRepository;
import com.redline.blog.services.CategoryService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("Category with the same name already exists");
        }

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            if (!category.get().getPosts().isEmpty()) {
                throw new IllegalStateException("Cannot delete category with associated posts");
            }
            categoryRepository.deleteById(categoryId);
        }
    }

    @Override
    public Category getCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
    }

}
