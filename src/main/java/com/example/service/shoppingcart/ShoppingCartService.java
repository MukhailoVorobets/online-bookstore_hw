package com.example.service.shoppingcart;

import com.example.dto.shoppingcart.AddToCartRequestDto;
import com.example.dto.shoppingcart.ShoppingCartDto;
import com.example.dto.shoppingcart.UpdateCartItemRequestDto;
import com.example.model.ShoppingCart;
import com.example.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto addCartItem(Long userId, AddToCartRequestDto request);

    ShoppingCartDto updateCartItem(Long userId, Long cartItemId,
                                   UpdateCartItemRequestDto requestDto);

    void deleteCartItem(Long userId, Long cartItemId);

    ShoppingCart createShoppingCart(User user);
}
