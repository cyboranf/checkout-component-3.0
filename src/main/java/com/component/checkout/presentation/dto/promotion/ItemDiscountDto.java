package com.component.checkout.presentation.dto.promotion;

public class ItemDiscountDto {

    private String bundleDiscountDetails;
    private String multiPricedDiscountDetails;

    private ItemDiscountDto(Builder builder) {
        this.bundleDiscountDetails = builder.bundleDiscountDetails;
        this.multiPricedDiscountDetails = builder.multiPricedDiscountDetails;
    }

    public void setBundleDiscountDetails(String bundleDiscountDetails) {
        this.bundleDiscountDetails = bundleDiscountDetails;
    }

    public void setMultiPricedDiscountDetails(String multiPricedDiscountDetails) {
        this.multiPricedDiscountDetails = multiPricedDiscountDetails;
    }

    public String getBundleDiscountDetails() {
        return bundleDiscountDetails;
    }

    public String getMultiPricedDiscountDetails() {
        return multiPricedDiscountDetails;
    }


    public static class Builder {

        private String bundleDiscountDetails;
        private String multiPricedDiscountDetails;

        public Builder withBundleDiscountDetails(String bundleDiscountDetails) {
            this.bundleDiscountDetails = bundleDiscountDetails;
            return this;
        }

        public Builder withMultiPricedDiscountDetails(String multiPricedDiscountDetails) {
            this.multiPricedDiscountDetails = multiPricedDiscountDetails;
            return this;
        }

        public ItemDiscountDto build() {
            return new ItemDiscountDto(this);
        }
    }
}
