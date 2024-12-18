package com.component.checkout.presentation.dto.promotion;

public class ItemDiscountDto {

    private String bundleDiscountDetails;
    private String multiPricedDiscountDetails;

    public String getBundleDiscountDetails() {
        return bundleDiscountDetails;
    }

    public void setBundleDiscountDetails(String bundleDiscountDetails) {
        this.bundleDiscountDetails = bundleDiscountDetails;
    }

    public String getMultiPricedDiscountDetails() {
        return multiPricedDiscountDetails;
    }

    public void setMultiPricedDiscountDetails(String multiPricedDiscountDetails) {
        this.multiPricedDiscountDetails = multiPricedDiscountDetails;
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
            ItemDiscountDto itemDiscountDto = new ItemDiscountDto();
            itemDiscountDto.setBundleDiscountDetails(this.bundleDiscountDetails);
            itemDiscountDto.setMultiPricedDiscountDetails(this.multiPricedDiscountDetails);
            return itemDiscountDto;
        }
    }
}
