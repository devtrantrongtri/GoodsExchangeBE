package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(int id);
    void createCategory(Category category);
    void updateCategory(Category category);
    void deleteCategoryById(int id);
}
