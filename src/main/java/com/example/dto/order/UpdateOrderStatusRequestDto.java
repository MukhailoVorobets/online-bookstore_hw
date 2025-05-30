package com.example.dto.order;

import com.example.model.Status;
import lombok.NonNull;

public record UpdateOrderStatusRequestDto(
        @NonNull
        Status status
) {
}
