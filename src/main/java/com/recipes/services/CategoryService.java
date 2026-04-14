package com.recipes.services;

import com.recipes.models.Category;
import com.recipes.models.User;
import com.recipes.repositories.CategoryRepository;
import com.recipes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Category> getAllCategories(Long userId) {
        return categoryRepository.findByUserId(userId);
    }

    public Optional<Category> getCategoryById(Long id, Long userId) {
        return categoryRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(userId));
    }

    public Category createCategory(Category category, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (categoryRepository.findByNameAndUserId(category.getName(), userId).isPresent()) {
            throw new RuntimeException("Category already exists: " + category.getName());
        }

        category.setUser(user);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updated, Long userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found: " + id));

        if (!category.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        category.setName(updated.getName());
        category.setDescription(updated.getDescription());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id, Long userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found: " + id));

        if (!category.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        category.setDeletedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    public List<Category> findUpdatedAfter(Long userId, LocalDateTime since) {
        return categoryRepository.findByUserIdUpdatedAfter(userId, since);
    }
}
