package com.uth.BE.Controller;

import com.uth.BE.Entity.Category;
import com.uth.BE.Service.Interface.ICategoryService;
import com.uth.BE.dto.req.CategoryPaginationRequest;
import com.uth.BE.dto.req.PaginationRequest;
import com.uth.BE.dto.res.GlobalRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final ICategoryService categoryService;

    @Autowired
    public CategoryController(ICategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/get_all_categories")
    public GlobalRes<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if (categories != null && !categories.isEmpty()) {
            return new GlobalRes<>(HttpStatus.OK, "Categories retrieved successfully", categories);
        } else {
            return new GlobalRes<>(HttpStatus.NO_CONTENT, "No categories found", null);
        }
    }


    @PostMapping("/create_category")
    public GlobalRes<String> createCategory(@RequestBody Map<String, Object> payload) {
        try {
            Object nameObj = payload.get("name");
            if (!(nameObj instanceof String)) {
                return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid name format");
            }
            String name = (String) nameObj;

            Category category = new Category();
            category.setName(name);

            categoryService.createCategory(category);
            return new GlobalRes<>(HttpStatus.CREATED, "Category created successfully");
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create category: " + e.getMessage());
        }
    }


    @PutMapping("update_category/{id}")
    public GlobalRes<String> updateCategory(@PathVariable int id, @RequestBody Map<String, Object> payload) {
        try {
            Optional<Category> existingCategory = categoryService.getCategoryById(id);
            if (existingCategory.isPresent()) {
                // Kiểm tra định dạng của trường name
                Object nameObj = payload.get("name");
                if (!(nameObj instanceof String)) {
                    return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid name format");
                }
                String name = (String) nameObj;

                // Cập nhật đối tượng Category
                Category category = existingCategory.get();
                category.setName(name);

                categoryService.updateCategory(category);
                return new GlobalRes<>(HttpStatus.OK, "Category updated successfully");
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "Category not found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update category: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete_category/{id}")
    public GlobalRes<String> deleteCategory(@PathVariable int id) {
        try {
            Optional<Category> existingCategory = categoryService.getCategoryById(id);
            if (existingCategory.isPresent()) {
                categoryService.deleteCategoryById(id);
                return new GlobalRes<>(HttpStatus.OK, "Category deleted successfully");
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "Category not found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete category: " + e.getMessage());
        }
    }

    @GetMapping("/getAllCategoriesWithPaginationAndSort")
    public GlobalRes<Page<Category>> getAllCategoriesWithPaginationAndSort(
            @Valid @RequestBody PaginationRequest request) {
        try {
            Page<Category> categoriesPage = categoryService.getAllCategoriesWithPaginationAndSort(
                    request.getOffset(), request.getPageSize(), request.getOrder(), request.getField());
            if (categoriesPage.hasContent()) {
                return new GlobalRes<>(HttpStatus.OK.value(), "Categories retrieved successfully", categoriesPage);
            } else {
                return new GlobalRes<>(HttpStatus.NO_CONTENT.value(), "No categories found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST.value(), "Invalid parameters: " + e.getMessage());
        }
    }


}
