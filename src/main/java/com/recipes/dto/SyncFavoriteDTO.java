package com.recipes.dto;

import java.time.LocalDateTime;

public class SyncFavoriteDTO {
    private Long id;
    private Long recipeId;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public SyncFavoriteDTO(Long recipeId, LocalDateTime updatedAt) {
        this.id = recipeId;
        this.recipeId = recipeId;
        this.updatedAt = updatedAt;
        this.deletedAt = null;
    }

    public Long getId() { return id; }
    public Long getRecipeId() { return recipeId; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
}
