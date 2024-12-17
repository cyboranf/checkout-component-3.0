package com.component.checkout.presentation.mapper;

import com.component.checkout.model.Cart;
import com.component.checkout.presentation.dto.cart.CartDto;

public class CartMapper {

    public static CartDto toDto(Cart cart) {
        return new CartDto.Builder()
                .withCartId(cart.getId())
                .withCartItems(cart.getCartItems())
                .withTotalPrice(cart.getTotalPrice())
                .build();
    }
}
