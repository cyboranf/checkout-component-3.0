package com.component.checkout.presentation.dto.receipt;

import com.component.checkout.presentation.dto.promotion.ReceiptDiscountDto;

import java.util.List;

public class ReceiptDto {

    private Long receiptId;
    private String issuedAt;
    private List<PurchasedItemDto> purchasedItems;
    private ReceiptDiscountDto receiptDiscount;

    public ReceiptDto() {
    }

    private ReceiptDto(Builder builder) {
        this.receiptId = builder.receiptId;
        this.issuedAt = builder.issuedAt;
        this.purchasedItems = builder.purchasedItems;
        this.receiptDiscount = builder.receiptDiscount;
    }

    public void setReceiptDiscount(ReceiptDiscountDto receiptDiscount) {
        this.receiptDiscount = receiptDiscount;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public void setPurchasedItems(List<PurchasedItemDto> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public List<PurchasedItemDto> getPurchasedItems() {
        return purchasedItems;
    }

    public ReceiptDiscountDto getReceiptDiscount() {
        return receiptDiscount;
    }

    public static class Builder {

        private Long receiptId;
        private String issuedAt;
        private List<PurchasedItemDto> purchasedItems;
        private ReceiptDiscountDto receiptDiscount;

        public Builder withReceiptId(Long receiptId) {
            this.receiptId = receiptId;
            return this;
        }

        public Builder withIssuedAt(String issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public Builder withPurchasedItems(List<PurchasedItemDto> purchasedItems) {
            this.purchasedItems = purchasedItems;
            return this;
        }

        public Builder withReceiptDiscount(ReceiptDiscountDto receiptDiscount) {
            this.receiptDiscount = receiptDiscount;
            return this;
        }

        public ReceiptDto build() {
            return new ReceiptDto(this);
        }
    }

    @Override
    public String toString() {
        return "ReceiptDto{" +
                "receiptId=" + receiptId +
                ", issuedAt='" + issuedAt + '\'' +
                ", purchasedItems=" + purchasedItems +
                ", receiptDiscount=" + receiptDiscount +
                '}';
    }
}
