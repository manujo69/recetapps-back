package com.recipes.repositories;

import com.recipes.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserId(Long userId);

    Optional<Category> findByNameAndUserId(String name, Long userId);

    List<Category> findAllByIdInAndUserId(List<Long> ids, Long userId);

    // Sync: includes soft-deleted records (bypasses @SQLRestriction via native query)
    @Query(value = "SELECT * FROM categories WHERE user_id = :userId AND updated_at > :since", nativeQuery = true)
    List<Category> findByUserIdUpdatedAfter(@Param("userId") Long userId, @Param("since") LocalDateTime since);

    // Sync: find by id regardless of deleted_at
    @Query(value = "SELECT * FROM categories WHERE id = :id", nativeQuery = true)
    Optional<Category> findByIdIncludingDeleted(@Param("id") Long id);
}
