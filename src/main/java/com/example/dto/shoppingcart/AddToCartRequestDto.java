package com.example.dto.shoppingcart;

import jakarta.validation.constraints.Positive;
import lombok.NonNull;

public record AddToCartRequestDto(
        @NonNull
        @Positive
        Long bookId,

        @Positive
        int quantity
) {
}
