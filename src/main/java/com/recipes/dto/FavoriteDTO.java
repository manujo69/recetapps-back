package com.recipes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

public class FavoriteDTO {

    private Long id;
    private Long recipeId;
    private LocalDateTime updatedAt;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private LocalDateTime deletedAt;

    public FavoriteDTO() {}

    public FavoriteDTO(Long id, Long recipeId, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.recipeId = recipeId;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
}
