package com.component.checkout.presentation.mapper;

import com.component.checkout.model.CartItem;
import com.component.checkout.model.Receipt;
import com.component.checkout.presentation.dto.promotion.ItemDiscountDto;
import com.component.checkout.presentation.dto.promotion.ReceiptDiscountDto;
import com.component.checkout.presentation.dto.receipt.PurchasedItemDto;
import com.component.checkout.presentation.dto.receipt.ReceiptDto;

import java.util.stream.Collectors;

/**
 * Maps Receipt entities and related data to ReceiptDto objects for presentation.
 */
public class ReceiptMapper {

    /**
     * Converts a Receipt and its associated pricing details into a ReceiptDto.
     * <p> FinalizePurchase / Checkout operation. </p>
     *
     * @param receipt                   The persisted Receipt entity.
     * @param totalPriceBeforeDiscounts The total price before any discounts.
     * @param totalDiscount             The total amount of discounts applied.
     * @param totalPriceWithDiscounts   The final price after discounts.
     * @return A ReceiptDto representing the finalized purchase.
     */
    public static ReceiptDto toDto(Receipt receipt,
                                   double totalPriceBeforeDiscounts,
                                   double totalDiscount,
                                   double totalPriceWithDiscounts) {
        return new ReceiptDto.Builder()
                .withReceiptId(receipt.getId())
                .withIssuedAt(receipt.getIssuedAt())
                .withPurchasedItems(
                        receipt.getPurchasedItems().stream()
                                .map(ReceiptMapper::toPurchasedItemDto)
                                .collect(Collectors.toList())
                )
                .withReceiptDiscount(toReceiptDiscountDto(
                        totalPriceBeforeDiscounts,
                        totalDiscount,
                        totalPriceWithDiscounts
                ))
                .build();
    }

    /**
     * Converts a single CartItem to a PurchasedItemDto, including discount details.
     */
    private static PurchasedItemDto toPurchasedItemDto(CartItem cartItem) {
        double priceBeforeDiscount = cartItem.getQuantity() * cartItem.getSingleNormalPrice();
        double priceWithDiscounts = (cartItem.getQuantitySpecialPrice() * cartItem.getSingleSpecialPrice()) +
                (cartItem.getQuantityNormalPrice() * cartItem.getSingleNormalPrice()) -
                cartItem.getBundleDiscount();

        return new PurchasedItemDto.Builder()
                .withItemName(cartItem.getItem().getName())
                .withQuantity(cartItem.getQuantity())
                .withPricePerOneItem(cartItem.getSingleNormalPrice())
                .withPriceBeforeDiscounts(priceBeforeDiscount)
                .withPriceWithDiscounts(priceWithDiscounts)
                .withItemDiscount(toItemDiscountDto(cartItem))
                .build();
    }

    /**
     * Builds an ItemDiscountDto with details about multi-pricing and bundle promotions.
     */
    private static ItemDiscountDto toItemDiscountDto(CartItem cartItem) {
        double multiPricedDiscount = (cartItem.getQuantitySpecialPrice() * cartItem.getSingleNormalPrice()) -
                (cartItem.getQuantitySpecialPrice() * cartItem.getSingleSpecialPrice());

        double bundleDiscount = cartItem.getBundleDiscount();
        int bundleDiscountQuantity = cartItem.getBundleDiscountQuantity();

        String bundleDiscountMessage = bundleDiscountQuantity > 0
                ? String.format("Saved %.2f due to bundle discount on %d items", bundleDiscount, bundleDiscountQuantity)
                : "No bundle discount";

        String multiPricedDiscountMessage = cartItem.getQuantitySpecialPrice() > 0
                ? String.format("Saved %.2f due to multi-price promo", multiPricedDiscount)
                : "No multi-price discount";

        return new ItemDiscountDto.Builder()
                .withBundleDiscountDetails(bundleDiscountMessage)
                .withMultiPricedDiscountDetails(multiPricedDiscountMessage)
                .build();
    }

    /**
     * Builds a ReceiptDiscountDto with aggregated discount details.
     */
    private static ReceiptDiscountDto toReceiptDiscountDto(double totalPriceBeforeDiscounts,
                                                           double totalDiscount,
                                                           double totalPriceWithDiscounts) {
        return new ReceiptDiscountDto.Builder()
                .withTotalPriceBeforeDiscounts(totalPriceBeforeDiscounts)
                .withTotalDiscount(totalDiscount)
                .withTotalPriceWithDiscounts(totalPriceWithDiscounts)
                .build();
    }
}