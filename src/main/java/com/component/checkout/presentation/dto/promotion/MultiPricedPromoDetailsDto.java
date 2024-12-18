package com.component.checkout.presentation.dto.promotion;

public class MultiPricedPromoDetailsDto {

    private double singleNormalPrice;
    private double singleSpecialPrice;
    private int quantityNormalPrice;
    private int quantitySpecialPrice;

    public double getSingleNormalPrice() {
        return singleNormalPrice;
    }

    public void setSingleNormalPrice(double singleNormalPrice) {
        this.singleNormalPrice = singleNormalPrice;
    }

    public double getSingleSpecialPrice() {
        return singleSpecialPrice;
    }

    public void setSingleSpecialPrice(double singleSpecialPrice) {
        this.singleSpecialPrice = singleSpecialPrice;
    }

    public int getQuantityNormalPrice() {
        return quantityNormalPrice;
    }

    public void setQuantityNormalPrice(int quantityNormalPrice) {
        this.quantityNormalPrice = quantityNormalPrice;
    }

    public int getQuantitySpecialPrice() {
        return quantitySpecialPrice;
    }

    public void setQuantitySpecialPrice(int quantitySpecialPrice) {
        this.quantitySpecialPrice = quantitySpecialPrice;
    }

    public static class Builder {
        private double singleNormalPrice;
        private double singleFinalPrice;
        private int quantityWithNormalPrice;
        private int quantityWithFinalPrice;

        public Builder withSingleNormalPrice(double singleNormalPrice) {
            this.singleNormalPrice = singleNormalPrice;
            return this;
        }

        public Builder withSingleFinalPrice(double singleFinalPrice) {
            this.singleFinalPrice = singleFinalPrice;
            return this;
        }

        public Builder withQuantityWithNormalPrice(int quantityWithNormalPrice) {
            this.quantityWithNormalPrice = quantityWithNormalPrice;
            return this;
        }

        public Builder withQuantityWithFinalPrice(int quantityWithFinalPrice) {
            this.quantityWithFinalPrice = quantityWithFinalPrice;
            return this;
        }

        public MultiPricedPromoDetailsDto build() {
            MultiPricedPromoDetailsDto dto = new MultiPricedPromoDetailsDto();
            dto.setSingleNormalPrice(this.singleNormalPrice);
            dto.setSingleSpecialPrice(this.singleFinalPrice);
            dto.setQuantityNormalPrice(this.quantityWithNormalPrice);
            dto.setQuantitySpecialPrice(this.quantityWithFinalPrice);
            return dto;
        }
    }
}