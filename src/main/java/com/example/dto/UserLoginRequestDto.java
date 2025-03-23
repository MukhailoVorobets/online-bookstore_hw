package com.example.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotEmpty
        @Size(min = 8, max = 20)
        String email,
        @NotEmpty
        @Size(min = 8, max = 20)
        String password
) {
}
