package com.component.checkout.presentation.dto.promotion;

public class ReceiptDiscountDto {

    private double totalPriceBeforeDiscounts;
    private double totalDiscount;
    private double totalPriceWithDiscounts;

    public ReceiptDiscountDto() {
    }

    private ReceiptDiscountDto(Builder builder) {
        this.totalPriceBeforeDiscounts = builder.totalPriceBeforeDiscounts;
        this.totalDiscount = builder.totalDiscount;
        this.totalPriceWithDiscounts = builder.totalPriceWithDiscounts;
    }

    public void setTotalPriceBeforeDiscounts(double totalPriceBeforeDiscounts) {
        this.totalPriceBeforeDiscounts = totalPriceBeforeDiscounts;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }


    public void setTotalPriceWithDiscounts(double totalPriceWithDiscounts) {
        this.totalPriceWithDiscounts = totalPriceWithDiscounts;
    }

    public double getTotalPriceBeforeDiscounts() {
        return totalPriceBeforeDiscounts;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public double getTotalPriceWithDiscounts() {
        return totalPriceWithDiscounts;
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
            return new ReceiptDiscountDto(this);
        }
    }

    @Override
    public String toString() {
        return "ReceiptDiscountDto{" +
                "totalPriceBeforeDiscounts=" + totalPriceBeforeDiscounts +
                ", totalDiscount=" + totalDiscount +
                ", totalPriceWithDiscounts=" + totalPriceWithDiscounts +
                '}';
    }
}
