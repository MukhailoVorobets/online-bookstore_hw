package com.example.dto.order;

import com.example.model.Order;
import lombok.NonNull;

public record UpdateOrderStatusRequestDto(
        @NonNull
        Order.Status status
) {
}
