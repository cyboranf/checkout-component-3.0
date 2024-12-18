package com.component.checkout.presentation.dto.promotion;

public class BundleDiscountsPromoDto {

    private int totalBundlePromoQuantity;
    private double totalBundleDiscount;

    public int getTotalBundlePromoQuantity() {
        return totalBundlePromoQuantity;
    }

    public void setTotalBundlePromoQuantity(int totalBundlePromoQuantity) {
        this.totalBundlePromoQuantity = totalBundlePromoQuantity;
    }

    public double getTotalBundleDiscount() {
        return totalBundleDiscount;
    }

    public void setTotalBundleDiscount(double totalBundleDiscount) {
        this.totalBundleDiscount = totalBundleDiscount;
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
            BundleDiscountsPromoDto promoDto = new BundleDiscountsPromoDto();
            promoDto.setTotalBundlePromoQuantity(this.totalBundlePromoQuantity);
            promoDto.setTotalBundleDiscount(this.totalBundleDiscount);
            return promoDto;
        }
    }
}
