package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.order.OrderItemsResponseDto;
import com.example.dto.order.OrderResponseDto;
import com.example.model.Order;
import com.example.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toDto(Order order);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemsResponseDto toItemDto(OrderItem item);
}
