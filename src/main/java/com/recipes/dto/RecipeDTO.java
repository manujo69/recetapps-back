package com.recipes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Recipe data including ingredients, instructions and metadata")
public class RecipeDTO {

    @Schema(description = "Recipe ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Recipe title", example = "Spaghetti Carbonara", maxLength = 200)
    @NotBlank
    @Size(max = 200)
    private String title;

    @Schema(description = "Short description of the recipe", example = "Classic Italian pasta dish", maxLength = 1000)
    @Size(max = 1000)
    private String description;

    @Schema(description = "List of ingredients (one per line)", example = "200g spaghetti\n100g pancetta\n2 eggs")
    @NotBlank
    private String ingredients;

    @Schema(description = "Step-by-step instructions", example = "1. Cook pasta\n2. Fry pancetta\n3. Mix eggs")
    @NotBlank
    private String instructions;

    @Schema(description = "Preparation time in minutes", example = "15")
    @NotNull
    private Integer prepTime;

    @Schema(description = "Cooking time in minutes", example = "20")
    @NotNull
    private Integer cookTime;

    @Schema(description = "Number of servings", example = "4")
    @NotNull
    private Integer servings;

    @Schema(description = "IDs of the categories this recipe belongs to", example = "[2, 5]")
    private List<Long> categoryIds;

    @Schema(description = "Category names (populated on read)", example = "[\"Pasta\", \"Italian\"]", accessMode = Schema.AccessMode.READ_ONLY)
    private List<String> categoryNames;

    @Schema(description = "ID of the user who created the recipe", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Username of the creator", example = "john_doe", accessMode = Schema.AccessMode.READ_ONLY)
    private String username;

    @Schema(description = "Images attached to this recipe", accessMode = Schema.AccessMode.READ_ONLY)
    private List<RecipeImageDTO> images;

    @Schema(description = "Creation timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @Schema(description = "Soft delete timestamp, null if active", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime deletedAt;

    // Constructors
    public RecipeDTO() {}

    // Getters and Setters
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