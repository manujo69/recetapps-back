package com.recipes.controllers;

import com.recipes.models.Recipe;
import com.recipes.models.RecipeImage;
import com.recipes.repositories.RecipeRepository;
import com.recipes.services.FileStorageService;
import com.recipes.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/recipes/{recipeId}/images")
@Tag(name = "Recipe Images", description = "Recipe image management APIs")
public class RecipeImageController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Add images to a recipe")
    public ResponseEntity<String> addImages(@PathVariable Long recipeId,
                                            @RequestParam(value = "image", required = false) MultipartFile image,
                                            @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                            Authentication authentication) throws IOException {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        String username = authentication.getName();
        Long userId = userService.getUserIdByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!recipe.getUser().getId().equals(userId)) {
            return ResponseEntity.status(403).body("Unauthorized");
        }

        List<MultipartFile> allFiles = new java.util.ArrayList<>();
        if (image != null) allFiles.add(image);
        if (files != null) allFiles.addAll(files);

        for (MultipartFile file : allFiles) {
            String filename = fileStorageService.storeFile(file);
            String url = "/recipes/" + recipeId + "/images/" + filename;
            RecipeImage recipeImage = new RecipeImage(filename, url, recipe);
            recipe.getImages().add(recipeImage);
        }

        recipeRepository.save(recipe);
        return ResponseEntity.ok("Images uploaded successfully");
    }

    @GetMapping("/{imageRef:.+}")
    @Operation(summary = "Get a specific image")
    public ResponseEntity<Resource> getImage(@PathVariable Long recipeId, @PathVariable String imageRef) throws IOException {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        RecipeImage image = recipe.getImages().stream()
                .filter(img -> img.getFilename().equals(imageRef) || String.valueOf(img.getId()).equals(imageRef))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Image not found"));

        Path filePath = fileStorageService.loadFile(image.getFilename());
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Could not read the file!");
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{imageRef:.+}")
    @Operation(summary = "Delete an image from a recipe")
    public ResponseEntity<Void> deleteImage(@PathVariable Long recipeId, @PathVariable String imageRef, Authentication authentication) throws IOException {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        String username = authentication.getName();
        Long userId = userService.getUserIdByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!recipe.getUser().getId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        RecipeImage image = recipe.getImages().stream()
                .filter(img -> img.getFilename().equals(imageRef) || String.valueOf(img.getId()).equals(imageRef))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Image not found"));

        recipe.getImages().remove(image);
        fileStorageService.deleteFile(image.getFilename());
        recipeRepository.save(recipe);

        return ResponseEntity.noContent().build();
    }
}