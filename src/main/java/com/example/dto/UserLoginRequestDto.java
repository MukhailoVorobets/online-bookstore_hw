package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank
        @Size(min = 8)
        String email,
        @NotBlank
        @Size(min = 8, max = 20)
        String password
) {
}
