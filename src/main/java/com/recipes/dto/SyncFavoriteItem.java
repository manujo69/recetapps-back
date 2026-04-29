package com.recipes.dto;

import java.time.LocalDateTime;

public class SyncFavoriteItem {
    private String clientId;
    private Long id;
    private Long recipeId;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
}
