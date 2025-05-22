package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.shoppingcart.AddToCartRequestDto;
import com.example.dto.shoppingcart.CartItemDto;
import com.example.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    CartItem toModel(AddToCartRequestDto addToCartRequestDto);
}
