package com.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Registration data for a new user")
public class AuthRequest {

    @Schema(description = "Unique username", example = "john_doe", maxLength = 50)
    @NotBlank
    @Size(max = 50)
    private String username;

    @Schema(description = "User email address", example = "john@example.com", maxLength = 100)
    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @Schema(description = "Password (min 6 characters)", example = "secret123", minLength = 6, maxLength = 255)
    @NotBlank
    @Size(min = 6, max = 255)
    private String password;

    // Constructors
    public AuthRequest() {}

    public AuthRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}