package com.recipes.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SyncRecipeDTO {
    private Long id;
    private String title;
    private String description;
    private String ingredients;
    private String instructions;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private List<Long> categoryIds;
    private List<String> categoryNames;
    private Long userId;
    private String username;
    private List<RecipeImageDTO> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

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
    public List<String> getCategoryNames() { return categoryNames; }
    public void setCategoryNames(List<String> categoryNames) { this.categoryNames = categoryNames; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public List<RecipeImageDTO> getImages() { return images; }
    public void setImages(List<RecipeImageDTO> images) { this.images = images; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
}
