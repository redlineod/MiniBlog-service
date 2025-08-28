package com.redline.blog.services;

import java.util.List;
import java.util.UUID;

import com.redline.blog.domain.entities.Category;

public interface CategoryService {
    List<Category> listCategories();
    Category createCategory(Category category);
    void deleteCategory(UUID categoryId);
    Category getCategoryById(UUID categoryId);
}
