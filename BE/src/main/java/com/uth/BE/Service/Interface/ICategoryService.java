package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(int id);
    void createCategory(Category category);
    void updateCategory(Category category);
    void deleteCategoryById(int id);
    Category findById(int id);

    Page<Category> getAllCategoriesWithPaginationAndSort(int pageNumber, int pageSize, String direction, String properties);
}
