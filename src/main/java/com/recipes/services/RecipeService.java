package com.recipes.services;

import com.recipes.dto.RecipeDTO;
import com.recipes.dto.RecipeImageDTO;
import com.recipes.dto.RecipeSummaryDTO;
import com.recipes.models.Category;
import com.recipes.models.Recipe;
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

    public List<RecipeSummaryDTO> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    public Optional<RecipeDTO> getRecipeById(Long id) {
        return recipeRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional
    public RecipeDTO createRecipe(RecipeDTO recipeDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Category> categories = recipeDTO.getCategoryIds() != null
                ? categoryRepository.findAllById(recipeDTO.getCategoryIds())
                : new java.util.ArrayList<>();

        Recipe recipe = new Recipe(
                recipeDTO.getTitle(),
                recipeDTO.getDescription(),
                recipeDTO.getIngredients(),
                recipeDTO.getInstructions(),
                recipeDTO.getPrepTime(),
                recipeDTO.getCookTime(),
                recipeDTO.getServings(),
                categories,
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

        if (recipeDTO.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(recipeDTO.getCategoryIds());
            recipe.setCategories(categories);
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

    public List<RecipeSummaryDTO> getRecipesByCategory(Long categoryId) {
        return recipeRepository.findByCategoriesId(categoryId).stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    public List<RecipeSummaryDTO> getRecipesByUser(Long userId) {
        return recipeRepository.findByUserId(userId).stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    public List<RecipeSummaryDTO> searchRecipes(String keyword) {
        return recipeRepository.searchByKeyword(keyword).stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    private RecipeSummaryDTO convertToSummaryDTO(Recipe recipe) {
        String firstImageUrl = recipe.getImages().isEmpty() ? null : recipe.getImages().get(0).getUrl();
        return new RecipeSummaryDTO(
                recipe.getId(),
                recipe.getTitle(),
                firstImageUrl,
                recipe.getPrepTime(),
                recipe.getCookTime(),
                recipe.getServings()
        );
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
        if (recipe.getCategories() != null && !recipe.getCategories().isEmpty()) {
            dto.setCategoryIds(recipe.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList()));
            dto.setCategoryNames(recipe.getCategories().stream()
                    .map(Category::getName)
                    .collect(Collectors.toList()));
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