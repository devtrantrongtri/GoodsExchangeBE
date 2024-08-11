package com.uth.BE.Controller;

import com.uth.BE.Entity.Category;
import com.uth.BE.Service.Interface.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final ICategoryService categoryService;

    @Autowired
    public CategoryController(ICategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategory();
        if (categories != null && !categories.isEmpty()) {
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        try {
            categoryService.createCategory(category);
            return new ResponseEntity<>("Category created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create category " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable int id, @RequestBody Category category) {
        try {
            Optional<Category> existingCategory = categoryService.getCategoryById(id);
            if (existingCategory.isPresent()) {
                category.setCategoryID(existingCategory.get().getCategoryID());
                categoryService.updateCategory(category);
                return new ResponseEntity<>("Category updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update category: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        try {
            Optional<Category> existingCategory = categoryService.getCategoryById(id);
            if (existingCategory.isPresent()) {
                categoryService.deleteCategoryById(id);
                return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete category: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
