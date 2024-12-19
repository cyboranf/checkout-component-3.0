package com.component.checkout.presentation.dto.promotion;

public class BundleDiscountsPromoDto {

    private int totalBundlePromoQuantity;
    private double totalBundleDiscount;

    private BundleDiscountsPromoDto(Builder builder) {
        this.totalBundlePromoQuantity = builder.totalBundlePromoQuantity;
        this.totalBundleDiscount = builder.totalBundleDiscount;
    }

    public void setTotalBundlePromoQuantity(int totalBundlePromoQuantity) {
        this.totalBundlePromoQuantity = totalBundlePromoQuantity;
    }

    public void setTotalBundleDiscount(double totalBundleDiscount) {
        this.totalBundleDiscount = totalBundleDiscount;
    }

    public int getTotalBundlePromoQuantity() {
        return totalBundlePromoQuantity;
    }

    public double getTotalBundleDiscount() {
        return totalBundleDiscount;
    }


    public static class Builder {
        private int totalBundlePromoQuantity;
        private double totalBundleDiscount;

        public Builder withTotalBundlePromoQuantity(int totalBundlePromoQuantity) {
            this.totalBundlePromoQuantity = totalBundlePromoQuantity;
            return this;
        }

        public Builder withTotalBundleDiscount(double totalBundleDiscount) {
            this.totalBundleDiscount = totalBundleDiscount;
            return this;
        }

        public BundleDiscountsPromoDto build() {
            return new BundleDiscountsPromoDto(this);
        }
    }
}
