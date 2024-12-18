package com.component.checkout.presentation.mapper;

import com.component.checkout.model.CartItem;
import com.component.checkout.presentation.dto.cartItem.CartItemDto;
import com.component.checkout.presentation.dto.promotion.MultiPricedPromoDetailsDto;

public class CartItemMapper {

    public static CartItemDto toDto(CartItem cartItem) {
        return new CartItemDto.Builder()
                .withCartItemId(cartItem.getId())
                .withItemId(cartItem.getItem().getId())
                .withCartId(cartItem.getCart() != null ? cartItem.getCart().getId() : null)
                .withQuantity(cartItem.getQuantity())
                .withMultiPricedPromoDetails(toMultiPricedDto(cartItem))
                .build();
    }

    public static MultiPricedPromoDetailsDto toMultiPricedDto(CartItem cartItem) {
        return new MultiPricedPromoDetailsDto.Builder()
                .withSingleNormalPrice(cartItem.getSingleNormalPrice())
                .withSingleFinalPrice(cartItem.getSingleSpecialPrice())
                .withQuantityWithNormalPrice(cartItem.getQuantityNormalPrice())
                .withQuantityWithFinalPrice(cartItem.getQuantitySpecialPrice())
                .build();
    }
}
