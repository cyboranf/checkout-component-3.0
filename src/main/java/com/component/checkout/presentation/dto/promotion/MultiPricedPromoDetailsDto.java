package com.component.checkout.presentation.dto.promotion;

public class MultiPricedPromoDetailsDto {

    private double singleNormalPrice;
    private double singleSpecialPrice;
    private int quantityNormalPrice;
    private int quantitySpecialPrice;

    private MultiPricedPromoDetailsDto(Builder builder) {
        this.singleNormalPrice = builder.singleNormalPrice;
        this.singleSpecialPrice = builder.singleSpecialPrice;
        this.quantityNormalPrice = builder.quantityNormalPrice;
        this.quantitySpecialPrice = builder.quantitySpecialPrice;
    }

    public void setSingleNormalPrice(double singleNormalPrice) {
        this.singleNormalPrice = singleNormalPrice;
    }

    public void setSingleSpecialPrice(double singleSpecialPrice) {
        this.singleSpecialPrice = singleSpecialPrice;
    }

    public void setQuantityNormalPrice(int quantityNormalPrice) {
        this.quantityNormalPrice = quantityNormalPrice;
    }

    public void setQuantitySpecialPrice(int quantitySpecialPrice) {
        this.quantitySpecialPrice = quantitySpecialPrice;
    }

    public double getSingleNormalPrice() {
        return singleNormalPrice;
    }

    public double getSingleSpecialPrice() {
        return singleSpecialPrice;
    }

    public int getQuantityNormalPrice() {
        return quantityNormalPrice;
    }

    public int getQuantitySpecialPrice() {
        return quantitySpecialPrice;
    }


    public static class Builder {

        private double singleNormalPrice;
        private double singleSpecialPrice;
        private int quantityNormalPrice;
        private int quantitySpecialPrice;

        public Builder withSingleNormalPrice(double singleNormalPrice) {
            this.singleNormalPrice = singleNormalPrice;
            return this;
        }

        public Builder withSingleFinalPrice(double singleSpecialPrice) {
            this.singleSpecialPrice = singleSpecialPrice;
            return this;
        }

        public Builder withQuantityWithNormalPrice(int quantityWithNormalPrice) {
            this.quantityNormalPrice = quantityWithNormalPrice;
            return this;
        }

        public Builder withQuantityWithFinalPrice(int quantityWithFinalPrice) {
            this.quantitySpecialPrice = quantityWithFinalPrice;
            return this;
        }

        public MultiPricedPromoDetailsDto build() {
            return new MultiPricedPromoDetailsDto(this);
        }
    }

    @Override
    public String toString() {
        return "MultiPricedPromoDetailsDto{" +
                "singleNormalPrice=" + singleNormalPrice +
                ", singleSpecialPrice=" + singleSpecialPrice +
                ", quantityNormalPrice=" + quantityNormalPrice +
                ", quantitySpecialPrice=" + quantitySpecialPrice +
                '}';
    }
}