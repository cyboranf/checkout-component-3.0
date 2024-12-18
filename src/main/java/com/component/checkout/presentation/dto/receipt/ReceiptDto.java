package com.component.checkout.presentation.dto.receipt;

import com.component.checkout.presentation.dto.cartItem.CartItemDto;

import java.util.Date;
import java.util.List;

public class ReceiptDto {

    private Long receiptId;
    private Date issuedAt;
    private List<CartItemDto> purchasedItems;
    private double totalAmount;

    public Long getReceiptId() {
        return receiptId;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public List<CartItemDto> getPurchasedItems() {
        return purchasedItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public void setPurchasedItems(List<CartItemDto> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public static class Builder {
        private Long receiptId;
        private Date issuedAt;
        private List<CartItemDto> purchasedItems;
        private double totalAmount;

        public Builder withReceiptId(Long receiptId) {
            this.receiptId = receiptId;
            return this;
        }

        public Builder withIssuedAt(Date issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public Builder withPurchasedItems(List<CartItemDto> purchasedItems) {
            this.purchasedItems = purchasedItems;
            return this;
        }

        public Builder withTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public ReceiptDto build() {
            ReceiptDto receiptDto = new ReceiptDto();
            receiptDto.setReceiptId(this.receiptId);
            receiptDto.setIssuedAt(this.issuedAt);
            receiptDto.setPurchasedItems(this.purchasedItems);
            receiptDto.setTotalAmount(this.totalAmount);
            return receiptDto;
        }
    }

    @Override
    public String toString() {
        return "ReceiptDto{" +
                "receiptId=" + receiptId +
                ", issuedAt=" + issuedAt +
                ", purchasedItems=" + purchasedItems +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
