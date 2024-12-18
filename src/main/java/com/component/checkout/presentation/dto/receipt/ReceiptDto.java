package com.component.checkout.presentation.dto.receipt;

import com.component.checkout.presentation.dto.promotion.ReceiptDiscountDto;

import java.util.Date;
import java.util.List;

public class ReceiptDto {

    private Long receiptId;
    private Date issuedAt;
    private List<PurchasedItemDto> purchasedItems;
    private ReceiptDiscountDto receiptDiscount;

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public List<PurchasedItemDto> getPurchasedItems() {
        return purchasedItems;
    }

    public void setPurchasedItems(List<PurchasedItemDto> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }

    public ReceiptDiscountDto getReceiptDiscount() {
        return receiptDiscount;
    }

    public void setReceiptDiscount(ReceiptDiscountDto receiptDiscount) {
        this.receiptDiscount = receiptDiscount;
    }

    public static class Builder {
        private Long receiptId;
        private Date issuedAt;
        private List<PurchasedItemDto> purchasedItems;
        private ReceiptDiscountDto receiptDiscount;

        public Builder withReceiptId(Long receiptId) {
            this.receiptId = receiptId;
            return this;
        }

        public Builder withIssuedAt(Date issuedAt) {
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
            ReceiptDto receiptDto = new ReceiptDto();
            receiptDto.setReceiptId(this.receiptId);
            receiptDto.setIssuedAt(this.issuedAt);
            receiptDto.setPurchasedItems(this.purchasedItems);
            receiptDto.setReceiptDiscount(this.receiptDiscount);
            return receiptDto;
        }
    }
}
