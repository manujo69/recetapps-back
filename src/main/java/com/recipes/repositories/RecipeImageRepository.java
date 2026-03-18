package com.recipes.repositories;

import com.recipes.models.RecipeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {

    List<RecipeImage> findByRecipeId(Long recipeId);
}