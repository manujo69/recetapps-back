package com.recipes.repositories;

import com.recipes.models.Favorite;
import com.recipes.models.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    List<Favorite> findByUserId(Long userId);

    boolean existsByIdUserIdAndIdRecipeId(Long userId, Long recipeId);

    void deleteByIdUserIdAndIdRecipeId(Long userId, Long recipeId);
}
