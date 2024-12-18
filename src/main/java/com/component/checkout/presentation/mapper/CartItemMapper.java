package com.component.checkout.presentation.mapper;

import com.component.checkout.model.CartItem;
import com.component.checkout.presentation.dto.cartItem.CartItemDto;

public class CartItemMapper {

    public static CartItemDto toDto(CartItem cartItem) {
        return new CartItemDto.Builder()
                .withCartItemId(cartItem.getId())
                .withItemId(cartItem.getItem().getId())
                .withCartId(cartItem.getCart().getId())
                .withQuantity(cartItem.getQuantity())
                .build();
    }
}
