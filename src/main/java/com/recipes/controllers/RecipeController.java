package com.recipes.controllers;

import com.recipes.dto.RecipeDTO;
import com.recipes.services.AuthService;
import com.recipes.services.RecipeService;
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
@RequestMapping("/recipes")
@Tag(name = "Recipes", description = "Recipe management APIs")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get all recipes")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
        List<RecipeDTO> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by ID")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new recipe")
    public ResponseEntity<RecipeDTO> createRecipe(@Valid @RequestBody RecipeDTO recipeDTO, Authentication authentication) {
        String username = authentication.getName();
        Long userId = getUserIdFromUsername(username);
        RecipeDTO createdRecipe = recipeService.createRecipe(recipeDTO, userId);
        return ResponseEntity.ok(createdRecipe);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing recipe")
    public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeDTO recipeDTO, Authentication authentication) {
        String username = authentication.getName();
        Long userId = getUserIdFromUsername(username);
        RecipeDTO updatedRecipe = recipeService.updateRecipe(id, recipeDTO, userId);
        return ResponseEntity.ok(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a recipe")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        Long userId = getUserIdFromUsername(username);
        recipeService.deleteRecipe(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get recipes by category")
    public ResponseEntity<List<RecipeDTO>> getRecipesByCategory(@PathVariable Long categoryId) {
        List<RecipeDTO> recipes = recipeService.getRecipesByCategory(categoryId);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get recipes by user")
    public ResponseEntity<List<RecipeDTO>> getRecipesByUser(@PathVariable Long userId) {
        List<RecipeDTO> recipes = recipeService.getRecipesByUser(userId);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/search")
    @Operation(summary = "Search recipes by keyword")
    public ResponseEntity<List<RecipeDTO>> searchRecipes(@RequestParam String keyword) {
        List<RecipeDTO> recipes = recipeService.searchRecipes(keyword);
        return ResponseEntity.ok(recipes);
    }

    private Long getUserIdFromUsername(String username) {
        return userService.getUserIdByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}