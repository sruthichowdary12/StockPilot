package com.inventory.stresstester.dto.auth;
import jakarta.validation.constraints.*;
public final class AuthDtos {
    private AuthDtos() {}
    public record RegisterRequest(@NotBlank String name, @Email @NotBlank String email,
        @NotBlank @Size(min=8, max=72) String password) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record UserResponse(Long id, String name, String email, String role) {}
    public record AuthResponse(String token, String tokenType, UserResponse user) {}
}