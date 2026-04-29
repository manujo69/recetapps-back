package com.recipes.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SyncRecipeItem {
    private String clientId;
    private Long id;
    private String title;
    private String description;
    private String ingredients;
    private String instructions;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private List<Long> categoryIds;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public Integer getPrepTime() { return prepTime; }
    public void setPrepTime(Integer prepTime) { this.prepTime = prepTime; }
    public Integer getCookTime() { return cookTime; }
    public void setCookTime(Integer cookTime) { this.cookTime = cookTime; }
    public Integer getServings() { return servings; }
    public void setServings(Integer servings) { this.servings = servings; }
    public List<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(List<Long> categoryIds) { this.categoryIds = categoryIds; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
}
