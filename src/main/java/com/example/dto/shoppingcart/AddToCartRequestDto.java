package com.example.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import lombok.NonNull;

public record AddToCartRequestDto(
        @NonNull
        Long bookId,

        @Min(1)
        int quantity
) {
}
