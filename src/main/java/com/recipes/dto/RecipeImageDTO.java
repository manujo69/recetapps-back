package com.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Image associated with a recipe")
public class RecipeImageDTO {

    @Schema(description = "Image ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Stored filename", example = "abc123.jpg", accessMode = Schema.AccessMode.READ_ONLY)
    private String filename;

    @Schema(description = "Public URL to access the image", example = "http://localhost:8080/recipes/1/images/1", accessMode = Schema.AccessMode.READ_ONLY)
    private String url;

    @Schema(description = "Upload timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    // Constructors
    public RecipeImageDTO() {}

    public RecipeImageDTO(Long id, String filename, String url, LocalDateTime createdAt) {
        this.id = id;
        this.filename = filename;
        this.url = url;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}