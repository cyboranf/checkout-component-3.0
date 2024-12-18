package com.component.checkout.presentation.mapper;

import com.component.checkout.model.Cart;
import com.component.checkout.presentation.dto.cart.CartDto;
import com.component.checkout.presentation.dto.cartItem.CartItemDto;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartDto toDto(Cart cart) {
        List<CartItemDto> cartItemDtos = cart.getCartItems().stream()
                .map(CartItemMapper::toDto)
                .collect(Collectors.toList());

        return new CartDto.Builder()
                .withCartId(cart.getId())
                .withCartItems(cartItemDtos)
                .withTotalPrice(cart.getTotalPriceWithDiscounts())
                .build();
    }
}
