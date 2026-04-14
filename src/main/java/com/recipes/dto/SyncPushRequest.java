package com.recipes.dto;

import java.util.ArrayList;
import java.util.List;

public class SyncPushRequest {

    private List<RecipePushItem> recipes = new ArrayList<>();
    private List<CategoryPushItem> categories = new ArrayList<>();
    private List<FavoritePushItem> favorites = new ArrayList<>();

    public List<RecipePushItem> getRecipes() { return recipes; }
    public void setRecipes(List<RecipePushItem> recipes) { this.recipes = recipes; }

    public List<CategoryPushItem> getCategories() { return categories; }
    public void setCategories(List<CategoryPushItem> categories) { this.categories = categories; }

    public List<FavoritePushItem> getFavorites() { return favorites; }
    public void setFavorites(List<FavoritePushItem> favorites) { this.favorites = favorites; }
}
