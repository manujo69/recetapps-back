-- V4__Sync_support.sql
-- Soft delete + per-user categories + favorites restructure for offline sync

-- 1. Soft delete on recipes
ALTER TABLE recipes
    ADD COLUMN deleted_at TIMESTAMP NULL DEFAULT NULL;

-- 2. Per-user categories: add user_id and soft delete, drop global unique on name
ALTER TABLE categories
    ADD COLUMN user_id  BIGINT       NULL,
    ADD COLUMN deleted_at TIMESTAMP  NULL DEFAULT NULL,
    ADD CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE categories
    DROP INDEX name;

-- Migrate existing categories to the first user
UPDATE categories
SET user_id = (SELECT id FROM users ORDER BY id LIMIT 1)
WHERE user_id IS NULL;

-- Make user_id NOT NULL after migration
ALTER TABLE categories
    MODIFY COLUMN user_id BIGINT NOT NULL;

-- 3. Restructure user_favorites: add surrogate PK, updated_at, deleted_at
--    Step A — drop FKs and composite PK so we can restructure
ALTER TABLE user_favorites
    DROP FOREIGN KEY user_favorites_ibfk_1,
    DROP FOREIGN KEY user_favorites_ibfk_2,
    DROP PRIMARY KEY;

--    Step B — add new columns
ALTER TABLE user_favorites
    ADD COLUMN id         BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST,
    ADD COLUMN updated_at TIMESTAMP NULL DEFAULT NULL,
    ADD COLUMN deleted_at TIMESTAMP NULL DEFAULT NULL;

--    Step C — seed updated_at from created_at for existing rows
UPDATE user_favorites SET updated_at = created_at WHERE updated_at IS NULL;

--    Step D — restore foreign keys
ALTER TABLE user_favorites
    ADD CONSTRAINT fk_favorites_user   FOREIGN KEY (user_id)   REFERENCES users(id)   ON DELETE CASCADE,
    ADD CONSTRAINT fk_favorites_recipe FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE;
