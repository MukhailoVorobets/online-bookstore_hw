package com.example.dto.shoppingcart;

import jakarta.validation.constraints.Positive;
import lombok.NonNull;

public record AddToCartRequestDto(
        @NonNull
        Long bookId,

        @Positive
        int quantity
) {
}
