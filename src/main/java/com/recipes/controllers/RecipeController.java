package com.recipes.controllers;

import com.recipes.dto.RecipeDTO;
import com.recipes.dto.RecipeSummaryDTO;
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
    @Operation(summary = "Get all recipes for the authenticated user")
    public ResponseEntity<List<RecipeSummaryDTO>> getAllRecipes(Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(recipeService.getAllRecipes(userId));
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
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(recipeService.createRecipe(recipeDTO, userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing recipe")
    public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeDTO recipeDTO, Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(recipeService.updateRecipe(id, recipeDTO, userId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a recipe")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserId(authentication);
        recipeService.deleteRecipe(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/categories")
    @Operation(summary = "Update categories of a recipe")
    public ResponseEntity<RecipeDTO> updateCategories(@PathVariable Long id, @RequestBody List<Long> categoryIds, Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(recipeService.updateCategories(id, categoryIds, userId));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get recipes by category")
    public ResponseEntity<List<RecipeSummaryDTO>> getRecipesByCategory(@PathVariable Long categoryId, Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(recipeService.getRecipesByCategory(categoryId, userId));
    }

    @GetMapping("/search")
    @Operation(summary = "Search recipes by keyword")
    public ResponseEntity<List<RecipeSummaryDTO>> searchRecipes(@RequestParam String keyword, Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(recipeService.searchRecipes(keyword, userId));
    }

    private Long getUserId(Authentication authentication) {
        return userService.getUserIdByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
