package com.recipes.repositories;

import com.recipes.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByCategoriesId(Long categoryId);

    List<Recipe> findByUserId(Long userId);

    @Query("SELECT r FROM Recipe r WHERE r.title LIKE %:keyword% OR r.description LIKE %:keyword% OR r.ingredients LIKE %:keyword%")
    List<Recipe> searchByKeyword(@Param("keyword") String keyword);
}