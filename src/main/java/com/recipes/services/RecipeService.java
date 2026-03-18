package com.recipes.services;

import com.recipes.dto.RecipeDTO;
import com.recipes.dto.RecipeImageDTO;
import com.recipes.models.Category;
import com.recipes.models.Recipe;
import com.recipes.models.RecipeImage;
import com.recipes.models.User;
import com.recipes.repositories.CategoryRepository;
import com.recipes.repositories.RecipeRepository;
import com.recipes.repositories.RecipeImageRepository;
import com.recipes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeImageRepository recipeImageRepository;

    public List<RecipeDTO> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<RecipeDTO> getRecipeById(Long id) {
        return recipeRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional
    public RecipeDTO createRecipe(RecipeDTO recipeDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = null;
        if (recipeDTO.getCategoryId() != null) {
            category = categoryRepository.findById(recipeDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        Recipe recipe = new Recipe(
                recipeDTO.getTitle(),
                recipeDTO.getDescription(),
                recipeDTO.getIngredients(),
                recipeDTO.getInstructions(),
                recipeDTO.getPrepTime(),
                recipeDTO.getCookTime(),
                recipeDTO.getServings(),
                category,
                user
        );

        Recipe savedRecipe = recipeRepository.save(recipe);
        return convertToDTO(savedRecipe);
    }

    @Transactional
    public RecipeDTO updateRecipe(Long id, RecipeDTO recipeDTO, Long userId) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (!recipe.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        if (recipeDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(recipeDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            recipe.setCategory(category);
        }

        recipe.setTitle(recipeDTO.getTitle());
        recipe.setDescription(recipeDTO.getDescription());
        recipe.setIngredients(recipeDTO.getIngredients());
        recipe.setInstructions(recipeDTO.getInstructions());
        recipe.setPrepTime(recipeDTO.getPrepTime());
        recipe.setCookTime(recipeDTO.getCookTime());
        recipe.setServings(recipeDTO.getServings());

        Recipe updatedRecipe = recipeRepository.save(recipe);
        return convertToDTO(updatedRecipe);
    }

    @Transactional
    public void deleteRecipe(Long id, Long userId) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (!recipe.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        recipeRepository.delete(recipe);
    }

    public List<RecipeDTO> getRecipesByCategory(Long categoryId) {
        return recipeRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<RecipeDTO> getRecipesByUser(Long userId) {
        return recipeRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<RecipeDTO> searchRecipes(String keyword) {
        return recipeRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RecipeDTO convertToDTO(Recipe recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getId());
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setIngredients(recipe.getIngredients());
        dto.setInstructions(recipe.getInstructions());
        dto.setPrepTime(recipe.getPrepTime());
        dto.setCookTime(recipe.getCookTime());
        dto.setServings(recipe.getServings());
        if (recipe.getCategory() != null) {
            dto.setCategoryId(recipe.getCategory().getId());
            dto.setCategoryName(recipe.getCategory().getName());
        }
        if (recipe.getUser() != null) {
            dto.setUserId(recipe.getUser().getId());
            dto.setUsername(recipe.getUser().getUsername());
        }
        dto.setImages(recipe.getImages().stream()
                .map(img -> new RecipeImageDTO(img.getId(), img.getFilename(), img.getUrl(), img.getCreatedAt()))
                .collect(Collectors.toList()));
        dto.setCreatedAt(recipe.getCreatedAt());
        dto.setUpdatedAt(recipe.getUpdatedAt());
        return dto;
    }
}