package com.recipes.dto;

import com.recipes.models.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SyncResponse {

    private List<RecipeDTO> recipes = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<FavoriteDTO> favorites = new ArrayList<>();
    private LocalDateTime serverTime;

    public List<RecipeDTO> getRecipes() { return recipes; }
    public void setRecipes(List<RecipeDTO> recipes) { this.recipes = recipes; }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }

    public List<FavoriteDTO> getFavorites() { return favorites; }
    public void setFavorites(List<FavoriteDTO> favorites) { this.favorites = favorites; }

    public LocalDateTime getServerTime() { return serverTime; }
    public void setServerTime(LocalDateTime serverTime) { this.serverTime = serverTime; }
}
