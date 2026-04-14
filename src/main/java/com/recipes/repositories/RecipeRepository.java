package com.recipes.repositories;

import com.recipes.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByUserId(Long userId);

    List<Recipe> findByCategoriesIdAndUserId(Long categoryId, Long userId);

    @Query("SELECT r FROM Recipe r WHERE r.user.id = :userId AND (r.title LIKE %:keyword% OR r.description LIKE %:keyword% OR r.ingredients LIKE %:keyword%)")
    List<Recipe> searchByKeywordAndUserId(@Param("keyword") String keyword, @Param("userId") Long userId);

    // Sync: includes soft-deleted records (bypasses @SQLRestriction via native query)
    @Query(value = "SELECT * FROM recipes WHERE user_id = :userId AND updated_at > :since", nativeQuery = true)
    List<Recipe> findByUserIdUpdatedAfter(@Param("userId") Long userId, @Param("since") LocalDateTime since);

    // Sync: find by id regardless of deleted_at
    @Query(value = "SELECT * FROM recipes WHERE id = :id", nativeQuery = true)
    java.util.Optional<Recipe> findByIdIncludingDeleted(@Param("id") Long id);
}
