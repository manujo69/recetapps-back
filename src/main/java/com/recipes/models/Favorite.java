package com.recipes.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_favorites")
public class Favorite {

    @EmbeddedId
    private FavoriteId id = new FavoriteId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Favorite() {}

    public Favorite(User user, Recipe recipe) {
        this.user = user;
        this.recipe = recipe;
        this.id = new FavoriteId(user.getId(), recipe.getId());
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public FavoriteId getId() { return id; }
    public void setId(FavoriteId id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
