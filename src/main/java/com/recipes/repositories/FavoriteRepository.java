package com.recipes.repositories;

import com.recipes.models.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserId(Long userId);

    // Only active (non-deleted) favorites — filtered by @SQLRestriction
    Optional<Favorite> findByUserIdAndRecipeId(Long userId, Long recipeId);

    // Sync: includes soft-deleted records (bypasses @SQLRestriction via native query)
    @Query(value = "SELECT * FROM user_favorites WHERE user_id = :userId AND updated_at > :since", nativeQuery = true)
    List<Favorite> findByUserIdUpdatedAfter(@Param("userId") Long userId, @Param("since") LocalDateTime since);

    // Needed to restore a soft-deleted favorite (bypasses @SQLRestriction)
    @Query(value = "SELECT * FROM user_favorites WHERE user_id = :userId AND recipe_id = :recipeId LIMIT 1", nativeQuery = true)
    Optional<Favorite> findByUserIdAndRecipeIdIncludingDeleted(@Param("userId") Long userId, @Param("recipeId") Long recipeId);
}
