package com.component.checkout.presentation.mapper;

import com.component.checkout.model.Cart;
import com.component.checkout.presentation.dto.cart.CartDto;
import com.component.checkout.presentation.dto.promotion.BundleDiscountsPromoDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

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
