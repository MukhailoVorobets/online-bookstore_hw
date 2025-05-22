package com.example.controller;

import com.example.dto.shoppingcart.AddToCartRequestDto;
import com.example.dto.shoppingcart.ShoppingCartDto;
import com.example.dto.shoppingcart.UpdateCartItemRequestDto;
import com.example.model.User;
import com.example.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart Management",
        description = "Endpoints for managing shopping card")
public class CartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get user's shopping cart",
            description = "Get user's shopping cart current user")
    public ShoppingCartDto getCart(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return shoppingCartService.getShoppingCart(userId);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add item to the shopping cart",
            description = "Add item to the shopping cart")
    public ShoppingCartDto addItemToShoppingCart(
            @RequestBody @Valid AddToCartRequestDto addToCartRequestDto,
            Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return shoppingCartService.addCartItem(userId, addToCartRequestDto);
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update cart item quantity",
            description = "Update cart item quantity")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto updateCartItemQuantity(@PathVariable Long cartItemId,
                                                  @RequestBody
                                                  @Valid UpdateCartItemRequestDto requestDto,
                                                  Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return shoppingCartService.updateCartItem(userId, cartItemId, requestDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Delete item",
            description = "Delete item from shopping cart")
    @PreAuthorize("hasRole('USER')")
    public void deleteItemFromShoppingCart(@PathVariable Long cartItemId,
                                           Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        shoppingCartService.deleteCartItem(userId, cartItemId);
    }

    private Long getCurrentUserId(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}
