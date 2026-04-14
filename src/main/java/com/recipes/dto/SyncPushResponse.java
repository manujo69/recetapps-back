package com.recipes.dto;

import java.util.ArrayList;
import java.util.List;

public class SyncPushResponse {

    private List<IdMapping> recipeMappings = new ArrayList<>();
    private List<IdMapping> categoryMappings = new ArrayList<>();
    private List<IdMapping> favoriteMappings = new ArrayList<>();

    public List<IdMapping> getRecipeMappings() { return recipeMappings; }
    public void setRecipeMappings(List<IdMapping> recipeMappings) { this.recipeMappings = recipeMappings; }

    public List<IdMapping> getCategoryMappings() { return categoryMappings; }
    public void setCategoryMappings(List<IdMapping> categoryMappings) { this.categoryMappings = categoryMappings; }

    public List<IdMapping> getFavoriteMappings() { return favoriteMappings; }
    public void setFavoriteMappings(List<IdMapping> favoriteMappings) { this.favoriteMappings = favoriteMappings; }
}
