-- V2__Recipe_many_categories.sql
-- Replace single category_id column with a many-to-many join table

CREATE TABLE recipe_categories (
    recipe_id   BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (recipe_id, category_id),
    FOREIGN KEY (recipe_id)   REFERENCES recipes(id)    ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- Migrate existing category assignments
INSERT INTO recipe_categories (recipe_id, category_id)
SELECT id, category_id
FROM recipes
WHERE category_id IS NOT NULL;

-- Remove old column
ALTER TABLE recipes DROP FOREIGN KEY recipes_ibfk_1;
ALTER TABLE recipes DROP COLUMN category_id;
