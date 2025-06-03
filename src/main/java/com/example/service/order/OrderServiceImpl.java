package com.example.service.order;

import com.example.dto.order.CreateOrderRequestDto;
import com.example.dto.order.OrderItemsResponseDto;
import com.example.dto.order.OrderResponseDto;
import com.example.exception.EntityNotFoundException;
import com.example.exception.OrderCreatingException;
import com.example.mapper.OrderMapper;
import com.example.model.CartItem;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.ShoppingCart;
import com.example.repository.order.OrderItemRepository;
import com.example.repository.order.OrderRepository;
import com.example.repository.shoppingcart.ShoppingCartRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto placeOrder(Long userId, CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Shopping card not found with id: " + userId));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderCreatingException("Can't create order because no cart items found");
        }
        Order order = fromOrder(shoppingCart, requestDto.shippingAddress());
        processOrderItems(order, shoppingCart);
        orderRepository.save(order);
        clearCart(shoppingCart);
        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderResponseDto> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public Page<OrderItemsResponseDto> getOrderItems(Long orderId, Long userId, Pageable pageable) {
        return orderItemRepository.findAllByOrderIdAndOrderUserId(orderId, userId, pageable)
                .map(orderMapper::toItemDto);
    }

    @Override
    public OrderItemsResponseDto getSpecificOrderItem(Long orderId, Long itemId, Long userId) {
        OrderItem item = orderItemRepository
                .findByIdAndOrderIdAndOrderUserId(itemId, orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found by itemId:"
                        + itemId));
        return orderMapper.toItemDto(item);
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, Order.Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Oder not found by id: " + orderId));
        order.setStatus(status);
        return orderMapper.toDto(orderRepository.save(order));
    }

    private Order fromOrder(ShoppingCart shoppingCart, String shippingAddress) {
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setShippingAddress(shippingAddress);
        order.setStatus(Order.Status.PENDING);
        return order;
    }

    private OrderItem createOrderItem(Order order, CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        orderItem.setOrder(order);
        return orderItem;
    }

    private void processOrderItems(Order order, ShoppingCart shoppingCart) {
        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem: shoppingCart.getCartItems()) {
            OrderItem orderItem = createOrderItem(order, cartItem);
            orderItems.add(orderItem);
            total = total.add(calculateItemTotal(orderItem));
        }
        order.setOrderItems(orderItems);
        order.setTotal(total);
    }

    private BigDecimal calculateItemTotal(OrderItem item) {
        return item.getBook().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    private void clearCart(ShoppingCart shoppingCart) {
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
    }
}
