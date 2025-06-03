package com.example.service.order;

import com.example.dto.order.CreateOrderRequestDto;
import com.example.dto.order.OrderItemsResponseDto;
import com.example.dto.order.OrderResponseDto;
import com.example.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto placeOrder(Long userId,CreateOrderRequestDto requestDto);

    Page<OrderResponseDto> getUserOrders(Long userId, Pageable pageable);

    Page<OrderItemsResponseDto> getOrderItems(Long orderId, Long userId, Pageable pageable);

    OrderItemsResponseDto getSpecificOrderItem(Long orderId, Long itemId, Long userId);

    OrderResponseDto updateOrderStatus(Long orderId, Order.Status status);
}
