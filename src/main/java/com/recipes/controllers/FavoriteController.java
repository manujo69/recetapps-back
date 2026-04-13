package com.recipes.controllers;

import com.recipes.dto.RecipeSummaryDTO;
import com.recipes.services.FavoriteService;
import com.recipes.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Favorites", description = "Favorite recipes management APIs")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserService userService;

    @PostMapping("/recipes/{id}/favorite")
    @Operation(summary = "Add recipe to favorites")
    public ResponseEntity<Void> addFavorite(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserId(authentication);
        favoriteService.addFavorite(userId, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/recipes/{id}/favorite")
    @Operation(summary = "Remove recipe from favorites")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserId(authentication);
        favoriteService.removeFavorite(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/me/favorites")
    @Operation(summary = "Get current user's favorite recipes")
    public ResponseEntity<List<RecipeSummaryDTO>> getMyFavorites(Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(favoriteService.getFavoritesByUser(userId));
    }

    @GetMapping("/recipes/{id}/favorite")
    @Operation(summary = "Check if recipe is in current user's favorites")
    public ResponseEntity<Map<String, Boolean>> isFavorite(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserId(authentication);
        boolean favorite = favoriteService.isFavorite(userId, id);
        return ResponseEntity.ok(Map.of("isFavorite", favorite));
    }

    private Long getUserId(Authentication authentication) {
        return userService.getUserIdByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
