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
public class CategoryController {
    private final ICategoryService categoryService;

        @Autowired
        public CategoryController(ICategoryService categoryService) {
            this.categoryService = categoryService;
        }

        @PostMapping("/createCategory")
        public ResponseEntity<Category> createCategory(@RequestBody Category category) {
            categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        }

        @GetMapping
        public ResponseEntity<List<Category>> getAllCategories() {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Category> getCategoryById(@PathVariable int id) {
            Optional<Category> category = categoryService.findById(id);
            return category.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }


        @GetMapping("/name/{name}")
        public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
            Optional<Category> category = categoryService.findByName(name);
            return category.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }

        @PutMapping("/updateCategory")
        public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
            try {
                categoryService.updateCategory(category);
                return ResponseEntity.ok(category);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteCategoryById(@PathVariable int id) {
            try {
                categoryService.deleteCategoryById(id);
                return ResponseEntity.noContent().build();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
}
