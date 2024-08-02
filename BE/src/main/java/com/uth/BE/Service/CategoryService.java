package com.uth.BE.Service;

import com.uth.BE.Pojo.Category;
import com.uth.BE.Repository.CategoryRepository;
import com.uth.BE.Service.Interface.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        try {
            return categoryRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error occurred while fetching all categories: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Optional<Category> getCategoryById(int id) {
        try {
            return categoryRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Error occurred while fetching category by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void createCategory(Category category) {
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            System.err.println("Error occurred while creating category: " + e.getMessage());
        }
    }

    @Override
    public void updateCategory(Category category) {
        try {
            if (categoryRepository.existsById(category.getCategoryId())) {
                categoryRepository.save(category);
            } else {
                System.err.println("Category with ID " + category.getCategoryId() + " does not exist.");
            }
        } catch (Exception e) {
            System.err.println("Error occurred while updating category: " + e.getMessage());
        }
    }

    @Override
    public void deleteCategoryById(int id) {
        try {
            if (categoryRepository.existsById(id)) {
                categoryRepository.deleteById(id);
            } else {
                System.err.println("Category with ID " + id + " does not exist.");
            }
        } catch (Exception e) {
            System.err.println("Error occurred while deleting category: " + e.getMessage());
        }
    }
}