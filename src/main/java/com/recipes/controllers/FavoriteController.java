package com.recipes.controllers;

import com.recipes.dto.FavoriteDTO;
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
import java.util.stream.Collectors;

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
    @Operation(summary = "Get current user's favorites")
    public ResponseEntity<List<FavoriteDTO>> getMyFavorites(Authentication authentication) {
        Long userId = getUserId(authentication);
        List<FavoriteDTO> favorites = favoriteService.getFavoritesByUser(userId).stream()
                .map(f -> new FavoriteDTO(f.getId(), f.getRecipe().getId(), f.getUpdatedAt(), f.getDeletedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/recipes/{id}/favorite")
    @Operation(summary = "Check if recipe is in current user's favorites")
    public ResponseEntity<Map<String, Boolean>> isFavorite(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(Map.of("isFavorite", favoriteService.isFavorite(userId, id)));
    }

    private Long getUserId(Authentication authentication) {
        return userService.getUserIdByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
