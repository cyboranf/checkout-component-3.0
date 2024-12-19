package com.component.checkout.presentation.mapper;

import com.component.checkout.model.Cart;
import com.component.checkout.presentation.dto.cart.CartDto;
import com.component.checkout.presentation.dto.promotion.BundleDiscountsPromoDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps Cart entities to CartDto objects, including bundle discount information.
 */
public class CartMapper {

    /**
     * Converts a Cart entity to a CartDto for presentation.
     *
     * @param cart The Cart entity to map.
     * @return A CartDto representing the current state of the cart.
     */
    public static CartDto toDto(Cart cart) {
        return new CartDto.Builder()
                .withCartId(cart.getId())
                .withCartItems(
                        cart.getCartItems().stream()
                                .map(CartItemMapper::toDto)
                                .collect(Collectors.toList())
                )
                .withTotalPrice(cart.getTotalPriceWithDiscounts())
                .withBundleDiscountsPromos(generateBundleDiscountsPromos(cart))
                .build();
    }

    /**
     * Generates a list of BundleDiscountsPromoDto if bundle promotions are applied.
     */
    private static List<BundleDiscountsPromoDto> generateBundleDiscountsPromos(Cart cart) {
        List<BundleDiscountsPromoDto> bundleDiscountsPromos = new ArrayList<>();
        if (cart.getTotalBundlePromoQuantity() > 0) {
            bundleDiscountsPromos.add(
                    new BundleDiscountsPromoDto.Builder()
                            .withTotalBundlePromoQuantity(cart.getTotalBundlePromoQuantity())
                            .withTotalBundleDiscount(cart.getTotalBundleDiscount())
                            .build()
            );
        }
        return bundleDiscountsPromos;
    }
}
