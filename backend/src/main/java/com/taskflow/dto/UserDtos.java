package com.taskflow.dto;

import jakarta.validation.constraints.*;

public final class UserDtos {
    private UserDtos() {
    }

    public record UserResponse(Long id, String name, String email) {
    }

    public record UpdateProfileRequest(@NotBlank @Size(max = 80) String name, @NotBlank @Email String email) {
    }
}
