package com.example.dto.order;

public record OrderItemsResponseDto(
        Long id,
        Long bookId,
        int quantity
) {
}
