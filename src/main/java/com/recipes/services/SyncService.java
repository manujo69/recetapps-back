package com.recipes.services;

import com.recipes.dto.*;
import com.recipes.models.Category;
import com.recipes.models.Favorite;
import com.recipes.models.Recipe;
import com.recipes.models.User;
import com.recipes.repositories.CategoryRepository;
import com.recipes.repositories.FavoriteRepository;
import com.recipes.repositories.RecipeRepository;
import com.recipes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SyncService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeService recipeService;

    public SyncResponse pull(Long userId, LocalDateTime since) {
        SyncResponse response = new SyncResponse();
        response.setServerTime(LocalDateTime.now());

        if (since == null) {
            // Full dataset
            response.setRecipes(recipeRepository.findByUserId(userId).stream()
                    .map(recipeService::convertToDTO)
                    .collect(Collectors.toList()));
            response.setCategories(categoryRepository.findByUserId(userId));
            response.setFavorites(favoriteRepository.findByUserId(userId).stream()
                    .map(f -> new FavoriteDTO(f.getId(), f.getRecipe().getId(), f.getUpdatedAt(), f.getDeletedAt()))
                    .collect(Collectors.toList()));
        } else {
            // Delta: everything updated after `since`, including soft-deleted
            response.setRecipes(recipeRepository.findByUserIdUpdatedAfter(userId, since).stream()
                    .map(recipeService::convertToDTO)
                    .collect(Collectors.toList()));
            response.setCategories(categoryRepository.findByUserIdUpdatedAfter(userId, since));
            response.setFavorites(favoriteRepository.findByUserIdUpdatedAfter(userId, since).stream()
                    .map(f -> new FavoriteDTO(f.getId(), f.getRecipe().getId(), f.getUpdatedAt(), f.getDeletedAt()))
                    .collect(Collectors.toList()));
        }

        return response;
    }

    @Transactional
    public SyncPushResponse push(Long userId, SyncPushRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SyncPushResponse response = new SyncPushResponse();

        response.setRecipeMappings(pushRecipes(request.getRecipes(), user));
        response.setCategoryMappings(pushCategories(request.getCategories(), user));
        response.setFavoriteMappings(pushFavorites(request.getFavorites(), user));

        return response;
    }

    private List<IdMapping> pushRecipes(List<RecipePushItem> items, User user) {
        List<IdMapping> mappings = new ArrayList<>();

        for (RecipePushItem item : items) {
            if (item.getId() != null) {
                // Update existing — last-write-wins
                recipeRepository.findByIdIncludingDeleted(item.getId()).ifPresent(recipe -> {
                    if (!recipe.getUser().getId().equals(user.getId())) return;
                    if (item.getUpdatedAt() != null && recipe.getUpdatedAt() != null
                            && item.getUpdatedAt().isAfter(recipe.getUpdatedAt())) {
                        applyRecipeFields(recipe, item, user);
                        recipeRepository.save(recipe);
                    }
                });
            } else {
                // New record — create and return id mapping
                Recipe recipe = new Recipe();
                recipe.setUser(user);
                applyRecipeFields(recipe, item, user);
                Recipe saved = recipeRepository.save(recipe);
                if (item.getClientId() != null) {
                    mappings.add(new IdMapping(item.getClientId(), saved.getId()));
                }
            }
        }

        return mappings;
    }

    private void applyRecipeFields(Recipe recipe, RecipePushItem item, User user) {
        recipe.setTitle(item.getTitle());
        recipe.setDescription(item.getDescription());
        recipe.setIngredients(item.getIngredients());
        recipe.setInstructions(item.getInstructions());
        recipe.setPrepTime(item.getPrepTime());
        recipe.setCookTime(item.getCookTime());
        recipe.setServings(item.getServings());
        recipe.setDeletedAt(item.getDeletedAt());

        if (item.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllByIdInAndUserId(item.getCategoryIds(), user.getId());
            recipe.setCategories(categories);
        }
    }

    private List<IdMapping> pushCategories(List<CategoryPushItem> items, User user) {
        List<IdMapping> mappings = new ArrayList<>();

        for (CategoryPushItem item : items) {
            if (item.getId() != null) {
                categoryRepository.findByIdIncludingDeleted(item.getId()).ifPresent(category -> {
                    if (!category.getUser().getId().equals(user.getId())) return;
                    if (item.getUpdatedAt() != null && category.getUpdatedAt() != null
                            && item.getUpdatedAt().isAfter(category.getUpdatedAt())) {
                        category.setName(item.getName());
                        category.setDescription(item.getDescription());
                        category.setDeletedAt(item.getDeletedAt());
                        categoryRepository.save(category);
                    }
                });
            } else {
                Category category = new Category(item.getName(), item.getDescription(), user);
                category.setDeletedAt(item.getDeletedAt());
                Category saved = categoryRepository.save(category);
                if (item.getClientId() != null) {
                    mappings.add(new IdMapping(item.getClientId(), saved.getId()));
                }
            }
        }

        return mappings;
    }

    private List<IdMapping> pushFavorites(List<FavoritePushItem> items, User user) {
        List<IdMapping> mappings = new ArrayList<>();

        for (FavoritePushItem item : items) {
            if (item.getId() != null) {
                favoriteRepository.findById(item.getId()).ifPresent(favorite -> {
                    if (!favorite.getUser().getId().equals(user.getId())) return;
                    if (item.getUpdatedAt() != null && favorite.getUpdatedAt() != null
                            && item.getUpdatedAt().isAfter(favorite.getUpdatedAt())) {
                        favorite.setDeletedAt(item.getDeletedAt());
                        favoriteRepository.save(favorite);
                    }
                });
            } else {
                recipeRepository.findById(item.getRecipeId()).ifPresent(recipe -> {
                    Favorite favorite = new Favorite(user, recipe);
                    favorite.setDeletedAt(item.getDeletedAt());
                    Favorite saved = favoriteRepository.save(favorite);
                    if (item.getClientId() != null) {
                        mappings.add(new IdMapping(item.getClientId(), saved.getId()));
                    }
                });
            }
        }

        return mappings;
    }
}
