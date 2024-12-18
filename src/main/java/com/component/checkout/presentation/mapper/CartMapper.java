package com.component.checkout.presentation.mapper;

import com.component.checkout.model.Cart;
import com.component.checkout.model.CartItem;
import com.component.checkout.presentation.dto.cart.CartDto;
import com.component.checkout.presentation.dto.cartItem.CartItemDto;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartDto toDto(Cart cart) {
        List<CartItemDto> cartItemDtos = cart.getCartItems().stream()
                .map(CartMapper::toCartItemDto)
                .collect(Collectors.toList());

        return new CartDto.Builder()
                .withCartId(cart.getId())
                .withCartItems(cartItemDtos)
                .withTotalPrice(cart.getTotalPrice())
                .build();
    }

    private static CartItemDto toCartItemDto(CartItem cartItem) {
        return new CartItemDto.Builder()
                .withCartItemId(cartItem.getId())
                .withItemId(cartItem.getItem().getId())
                .withCartId(cartItem.getCart().getId())
                .withQuantity(cartItem.getQuantity())
                .build();
    }
}
