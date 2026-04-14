package com.recipes.controllers;

import com.recipes.models.Category;
import com.recipes.services.CategoryService;
import com.recipes.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Category management APIs")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get all categories for the authenticated user")
    public ResponseEntity<List<Category>> getAllCategories(Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(categoryService.getAllCategories(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserId(authentication);
        return categoryService.getCategoryById(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category, Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(categoryService.createCategory(category, userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody Category category, Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(categoryService.updateCategory(id, category, userId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a category")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserId(authentication);
        categoryService.deleteCategory(id, userId);
        return ResponseEntity.noContent().build();
    }

    private Long getUserId(Authentication authentication) {
        return userService.getUserIdByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
