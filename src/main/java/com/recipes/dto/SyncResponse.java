package com.recipes.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SyncResponse {
    private List<SyncRecipeDTO> recipes;
    private List<SyncCategoryDTO> categories;
    private List<SyncFavoriteDTO> favorites;
    private LocalDateTime serverTime;

    public SyncResponse(List<SyncRecipeDTO> recipes, List<SyncCategoryDTO> categories,
                        List<SyncFavoriteDTO> favorites, LocalDateTime serverTime) {
        this.recipes = recipes;
        this.categories = categories;
        this.favorites = favorites;
        this.serverTime = serverTime;
    }

    public List<SyncRecipeDTO> getRecipes() { return recipes; }
    public List<SyncCategoryDTO> getCategories() { return categories; }
    public List<SyncFavoriteDTO> getFavorites() { return favorites; }
    public LocalDateTime getServerTime() { return serverTime; }
}
