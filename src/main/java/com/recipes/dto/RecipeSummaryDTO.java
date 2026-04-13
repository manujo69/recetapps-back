package com.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Recipe summary for list views")
public class RecipeSummaryDTO {

    @Schema(description = "Recipe ID", example = "1")
    private Long id;

    @Schema(description = "Recipe title", example = "Spaghetti Carbonara")
    private String title;

    @Schema(description = "URL of the first image, or null if none", example = "http://localhost:8080/recipes/1/images/1")
    private String firstImageUrl;

    @Schema(description = "Preparation time in minutes", example = "15")
    private Integer prepTime;

    @Schema(description = "Cooking time in minutes", example = "20")
    private Integer cookTime;

    @Schema(description = "Number of servings", example = "4")
    private Integer servings;

    @Schema(description = "IDs of the categories this recipe belongs to", example = "[2, 5]")
    private List<Long> categoryIds;

    public RecipeSummaryDTO() {}

    public RecipeSummaryDTO(Long id, String title, String firstImageUrl, Integer prepTime, Integer cookTime, Integer servings, List<Long> categoryIds) {
        this.id = id;
        this.title = title;
        this.firstImageUrl = firstImageUrl;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servings = servings;
        this.categoryIds = categoryIds;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getFirstImageUrl() { return firstImageUrl; }
    public void setFirstImageUrl(String firstImageUrl) { this.firstImageUrl = firstImageUrl; }

    public Integer getPrepTime() { return prepTime; }
    public void setPrepTime(Integer prepTime) { this.prepTime = prepTime; }

    public Integer getCookTime() { return cookTime; }
    public void setCookTime(Integer cookTime) { this.cookTime = cookTime; }

    public Integer getServings() { return servings; }
    public void setServings(Integer servings) { this.servings = servings; }

    public List<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(List<Long> categoryIds) { this.categoryIds = categoryIds; }
}
