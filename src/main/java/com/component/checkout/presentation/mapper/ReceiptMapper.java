package com.component.checkout.presentation.mapper;

import com.component.checkout.model.Receipt;
import com.component.checkout.presentation.dto.receipt.ReceiptDto;

public class ReceiptMapper {

    public static ReceiptDto toDto(Receipt receipt) {
        return new ReceiptDto.Builder()
                .withReceiptId(receipt.getId())
                .withIssuedAt(receipt.getIssuedAt())
                .withPurchasedItems(receipt.getPurchasedItems().stream()
                        .map(CartItemMapper::toDto)
                        .toList())
                .withTotalAmount(receipt.getTotalAmount())
                .build();
    }
}
