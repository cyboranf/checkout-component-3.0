package com.component.checkout.presentation.dto.promotion;

public class ReceiptDiscountDto {

    private double totalPriceBeforeDiscounts;
    private double totalDiscount;
    private double totalPriceWithDiscounts;

    public double getTotalPriceBeforeDiscounts() {
        return totalPriceBeforeDiscounts;
    }

    public void setTotalPriceBeforeDiscounts(double totalPriceBeforeDiscounts) {
        this.totalPriceBeforeDiscounts = totalPriceBeforeDiscounts;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalPriceWithDiscounts() {
        return totalPriceWithDiscounts;
    }

    public void setTotalPriceWithDiscounts(double totalPriceWithDiscounts) {
        this.totalPriceWithDiscounts = totalPriceWithDiscounts;
    }

    public static class Builder {
        private double totalPriceBeforeDiscounts;
        private double totalDiscount;
        private double totalPriceWithDiscounts;

        public Builder withTotalPriceBeforeDiscounts(double totalPriceBeforeDiscounts) {
            this.totalPriceBeforeDiscounts = totalPriceBeforeDiscounts;
            return this;
        }

        public Builder withTotalDiscount(double totalDiscount) {
            this.totalDiscount = totalDiscount;
            return this;
        }

        public Builder withTotalPriceWithDiscounts(double totalPriceWithDiscounts) {
            this.totalPriceWithDiscounts = totalPriceWithDiscounts;
            return this;
        }

        public ReceiptDiscountDto build() {
            ReceiptDiscountDto receiptDiscountDto = new ReceiptDiscountDto();
            receiptDiscountDto.setTotalPriceBeforeDiscounts(this.totalPriceBeforeDiscounts);
            receiptDiscountDto.setTotalDiscount(this.totalDiscount);
            receiptDiscountDto.setTotalPriceWithDiscounts(this.totalPriceWithDiscounts);
            return receiptDiscountDto;
        }
    }
}
