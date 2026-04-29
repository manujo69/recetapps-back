package com.recipes.services;

import com.recipes.dto.*;
import com.recipes.models.Category;
import com.recipes.models.Favorite;
import com.recipes.models.Recipe;
import com.recipes.models.User;
import com.recipes.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SyncService {

    @Autowired private RecipeRepository recipeRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private FavoriteRepository favoriteRepository;
    @Autowired private UserRepository userRepository;

    public SyncResponse getSync(Long userId) {
        List<SyncRecipeDTO> recipes = recipeRepository.findByUserId(userId).stream()
                .map(this::toSyncRecipeDTO)
                .collect(Collectors.toList());

        List<SyncCategoryDTO> categories = categoryRepository.findAll().stream()
                .map(this::toSyncCategoryDTO)
                .collect(Collectors.toList());

        List<SyncFavoriteDTO> favorites = favoriteRepository.findByUserId(userId).stream()
                .map(f -> new SyncFavoriteDTO(f.getRecipe().getId(), f.getCreatedAt()))
                .collect(Collectors.toList());

        return new SyncResponse(recipes, categories, favorites, LocalDateTime.now());
    }

    @Transactional
    public SyncResult processSync(SyncRequest request, Long userId) {
        List<IdMapping> recipeMappings = processRecipes(request.getRecipes(), userId);
        List<IdMapping> categoryMappings = processCategories(request.getCategories());
        List<IdMapping> favoriteMappings = processFavorites(request.getFavorites(), userId);
        return new SyncResult(recipeMappings, categoryMappings, favoriteMappings);
    }

    private List<IdMapping> processRecipes(List<SyncRecipeItem> items, Long userId) {
        List<IdMapping> mappings = new ArrayList<>();
        if (items == null) return mappings;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (SyncRecipeItem item : items) {
            if (item.getDeletedAt() != null) {
                if (item.getId() != null && item.getId() > 0) {
                    recipeRepository.findById(item.getId()).ifPresent(r -> {
                        if (r.getUser().getId().equals(userId)) recipeRepository.delete(r);
                    });
                }
            } else if (item.getId() == null || item.getId() == 0) {
                List<Category> cats = item.getCategoryIds() != null
                        ? categoryRepository.findAllById(item.getCategoryIds()) : new ArrayList<>();
                Recipe recipe = new Recipe(item.getTitle(), item.getDescription(),
                        item.getIngredients(), item.getInstructions(),
                        item.getPrepTime(), item.getCookTime(), item.getServings(), cats, user);
                Recipe saved = recipeRepository.save(recipe);
                mappings.add(new IdMapping(item.getClientId(), saved.getId()));
            } else {
                recipeRepository.findById(item.getId()).ifPresent(recipe -> {
                    if (!recipe.getUser().getId().equals(userId)) return;
                    if (item.getCategoryIds() != null) {
                        recipe.setCategories(categoryRepository.findAllById(item.getCategoryIds()));
                    }
                    recipe.setTitle(item.getTitle());
                    recipe.setDescription(item.getDescription());
                    recipe.setIngredients(item.getIngredients());
                    recipe.setInstructions(item.getInstructions());
                    recipe.setPrepTime(item.getPrepTime());
                    recipe.setCookTime(item.getCookTime());
                    recipe.setServings(item.getServings());
                    recipeRepository.save(recipe);
                });
            }
        }
        return mappings;
    }

    private List<IdMapping> processCategories(List<SyncCategoryItem> items) {
        List<IdMapping> mappings = new ArrayList<>();
        if (items == null) return mappings;

        for (SyncCategoryItem item : items) {
            if (item.getDeletedAt() != null) {
                if (item.getId() != null && item.getId() > 0) {
                    categoryRepository.deleteById(item.getId());
                }
            } else if (item.getId() == null || item.getId() == 0) {
                Category cat = new Category(item.getName(), item.getDescription());
                Category saved = categoryRepository.save(cat);
                mappings.add(new IdMapping(item.getClientId(), saved.getId()));
            } else {
                categoryRepository.findById(item.getId()).ifPresent(cat -> {
                    cat.setName(item.getName());
                    cat.setDescription(item.getDescription());
                    categoryRepository.save(cat);
                });
            }
        }
        return mappings;
    }

    private List<IdMapping> processFavorites(List<SyncFavoriteItem> items, Long userId) {
        List<IdMapping> mappings = new ArrayList<>();
        if (items == null) return mappings;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (SyncFavoriteItem item : items) {
            Long recipeId = item.getRecipeId() != null ? item.getRecipeId() : item.getId();
            if (recipeId == null) continue;

            if (item.getDeletedAt() != null) {
                favoriteRepository.deleteByIdUserIdAndIdRecipeId(userId, recipeId);
            } else if (!favoriteRepository.existsByIdUserIdAndIdRecipeId(userId, recipeId)) {
                recipeRepository.findById(recipeId).ifPresent(recipe -> {
                    favoriteRepository.save(new Favorite(user, recipe));
                    if (item.getClientId() != null) {
                        mappings.add(new IdMapping(item.getClientId(), recipeId));
                    }
                });
            }
        }
        return mappings;
    }

    private SyncRecipeDTO toSyncRecipeDTO(Recipe recipe) {
        SyncRecipeDTO dto = new SyncRecipeDTO();
        dto.setId(recipe.getId());
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setIngredients(recipe.getIngredients());
        dto.setInstructions(recipe.getInstructions());
        dto.setPrepTime(recipe.getPrepTime());
        dto.setCookTime(recipe.getCookTime());
        dto.setServings(recipe.getServings());
        if (recipe.getCategories() != null) {
            dto.setCategoryIds(recipe.getCategories().stream().map(Category::getId).collect(Collectors.toList()));
            dto.setCategoryNames(recipe.getCategories().stream().map(Category::getName).collect(Collectors.toList()));
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
        dto.setDeletedAt(null);
        return dto;
    }

    private SyncCategoryDTO toSyncCategoryDTO(Category cat) {
        SyncCategoryDTO dto = new SyncCategoryDTO();
        dto.setId(cat.getId());
        dto.setName(cat.getName());
        dto.setDescription(cat.getDescription());
        dto.setCreatedAt(cat.getCreatedAt());
        dto.setUpdatedAt(cat.getUpdatedAt());
        dto.setDeletedAt(null);
        return dto;
    }
}
