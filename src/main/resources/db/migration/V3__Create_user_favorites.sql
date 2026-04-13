-- V3__Create_user_favorites.sql
-- Create user_favorites table for the Favorite entity

CREATE TABLE user_favorites (
    user_id   BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    created_at DATETIME,
    PRIMARY KEY (user_id, recipe_id),
    FOREIGN KEY (user_id)   REFERENCES users(id)   ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);
