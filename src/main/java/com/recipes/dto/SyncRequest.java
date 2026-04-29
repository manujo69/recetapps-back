package com.recipes.dto;

import java.util.List;

public class SyncRequest {
    private List<SyncRecipeItem> recipes;
    private List<SyncCategoryItem> categories;
    private List<SyncFavoriteItem> favorites;

    public List<SyncRecipeItem> getRecipes() { return recipes; }
    public void setRecipes(List<SyncRecipeItem> recipes) { this.recipes = recipes; }
    public List<SyncCategoryItem> getCategories() { return categories; }
    public void setCategories(List<SyncCategoryItem> categories) { this.categories = categories; }
    public List<SyncFavoriteItem> getFavorites() { return favorites; }
    public void setFavorites(List<SyncFavoriteItem> favorites) { this.favorites = favorites; }
}
