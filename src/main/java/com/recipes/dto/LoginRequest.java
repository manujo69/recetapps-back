package com.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Credentials for user authentication")
public class LoginRequest {

    @Schema(description = "User email address", example = "john@example.com")
    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @Schema(description = "User password", example = "secret123", minLength = 6, maxLength = 255)
    @NotBlank
    @Size(min = 6, max = 255)
    private String password;

    public LoginRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
