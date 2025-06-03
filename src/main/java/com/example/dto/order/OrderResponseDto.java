package com.example.dto.order;

import com.example.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long id,
        Long userId,
        List<OrderItemsResponseDto> orderItems,
        LocalDateTime orderDate,
        BigDecimal total,
        Order.Status status
) {
}
