package com.recipes.services;

import com.recipes.dto.RecipeSummaryDTO;
import com.recipes.models.Category;
import com.recipes.models.Favorite;
import com.recipes.models.Recipe;
import com.recipes.models.User;
import com.recipes.repositories.FavoriteRepository;
import com.recipes.repositories.RecipeRepository;
import com.recipes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Transactional
    public void addFavorite(Long userId, Long recipeId) {
        if (favoriteRepository.existsByIdUserIdAndIdRecipeId(userId, recipeId)) {
            throw new RuntimeException("Recipe is already in favorites");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        favoriteRepository.save(new Favorite(user, recipe));
    }

    @Transactional
    public void removeFavorite(Long userId, Long recipeId) {
        if (!favoriteRepository.existsByIdUserIdAndIdRecipeId(userId, recipeId)) {
            throw new RuntimeException("Recipe is not in favorites");
        }
        favoriteRepository.deleteByIdUserIdAndIdRecipeId(userId, recipeId);
    }

    public List<RecipeSummaryDTO> getFavoritesByUser(Long userId) {
        return favoriteRepository.findByUserId(userId).stream()
                .map(fav -> {
                    Recipe r = fav.getRecipe();
                    String firstImageUrl = r.getImages().isEmpty() ? null : r.getImages().get(0).getUrl();
                    List<Long> categoryIds = r.getCategories() != null
                            ? r.getCategories().stream().map(c -> c.getId()).collect(Collectors.toList())
                            : new java.util.ArrayList<>();
                    return new RecipeSummaryDTO(r.getId(), r.getTitle(), firstImageUrl, r.getPrepTime(), r.getCookTime(), r.getServings(), categoryIds);
                })
                .collect(Collectors.toList());
    }

    public boolean isFavorite(Long userId, Long recipeId) {
        return favoriteRepository.existsByIdUserIdAndIdRecipeId(userId, recipeId);
    }
}
