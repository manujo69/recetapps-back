package com.recipes.services;

import com.recipes.models.Favorite;
import com.recipes.models.Recipe;
import com.recipes.models.User;
import com.recipes.repositories.FavoriteRepository;
import com.recipes.repositories.RecipeRepository;
import com.recipes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        // Check if an active favorite already exists
        if (favoriteRepository.findByUserIdAndRecipeId(userId, recipeId).isPresent()) {
            throw new RuntimeException("Recipe is already in favorites");
        }

        // Restore a soft-deleted favorite if one exists
        Optional<Favorite> existing = favoriteRepository.findByUserIdAndRecipeIdIncludingDeleted(userId, recipeId);
        if (existing.isPresent()) {
            Favorite favorite = existing.get();
            favorite.setDeletedAt(null);
            favoriteRepository.save(favorite);
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        favoriteRepository.save(new Favorite(user, recipe));
    }

    @Transactional
    public void removeFavorite(Long userId, Long recipeId) {
        Favorite favorite = favoriteRepository.findByUserIdAndRecipeId(userId, recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe is not in favorites"));

        favorite.setDeletedAt(LocalDateTime.now());
        favoriteRepository.save(favorite);
    }

    public List<Favorite> getFavoritesByUser(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public boolean isFavorite(Long userId, Long recipeId) {
        return favoriteRepository.findByUserIdAndRecipeId(userId, recipeId).isPresent();
    }

    public List<Favorite> findUpdatedAfter(Long userId, LocalDateTime since) {
        return favoriteRepository.findByUserIdUpdatedAfter(userId, since);
    }
}
