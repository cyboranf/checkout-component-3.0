package com.component.checkout.presentation.mapper;

import com.component.checkout.model.CartItem;
import com.component.checkout.presentation.dto.cartItem.CartItemDto;
import com.component.checkout.presentation.dto.promotion.MultiPricedPromoDetailsDto;

/**
 * Maps CartItem entities to CartItemDto objects.
 */
public class CartItemMapper {

    /**
     * Converts a CartItem to a CartItemDto.
     *
     * @param cartItem The CartItem entity to map.
     * @return A CartItemDto representing the cart item's state.
     */
    public static CartItemDto toDto(CartItem cartItem) {
        return new CartItemDto.Builder()
                .withCartItemId(cartItem.getId())
                .withItemId(cartItem.getItem().getId())
                .withCartId(cartItem.getCart() != null ? cartItem.getCart().getId() : null)
                .withQuantity(cartItem.getQuantity())
                .withMultiPricedPromoDetails(toMultiPricedDto(cartItem))
                .build();
    }

    /**
     * Extracts multi-priced promotional details from the CartItem.
     */
    public static MultiPricedPromoDetailsDto toMultiPricedDto(CartItem cartItem) {
        return new MultiPricedPromoDetailsDto.Builder()
                .withSingleNormalPrice(cartItem.getSingleNormalPrice())
                .withSingleFinalPrice(cartItem.getSingleSpecialPrice())
                .withQuantityWithNormalPrice(cartItem.getQuantityNormalPrice())
                .withQuantityWithFinalPrice(cartItem.getQuantitySpecialPrice())
                .build();
    }
}
