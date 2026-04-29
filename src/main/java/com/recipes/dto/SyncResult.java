package com.recipes.dto;

import java.util.List;

public class SyncResult {
    private List<IdMapping> recipeMappings;
    private List<IdMapping> categoryMappings;
    private List<IdMapping> favoriteMappings;

    public SyncResult(List<IdMapping> recipeMappings, List<IdMapping> categoryMappings, List<IdMapping> favoriteMappings) {
        this.recipeMappings = recipeMappings;
        this.categoryMappings = categoryMappings;
        this.favoriteMappings = favoriteMappings;
    }

    public List<IdMapping> getRecipeMappings() { return recipeMappings; }
    public List<IdMapping> getCategoryMappings() { return categoryMappings; }
    public List<IdMapping> getFavoriteMappings() { return favoriteMappings; }
}
