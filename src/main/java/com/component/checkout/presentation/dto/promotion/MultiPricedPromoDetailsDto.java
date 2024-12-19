package com.component.checkout.presentation.dto.promotion;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO representing details for multi-priced promotions.
 */
public class MultiPricedPromoDetailsDto {

    private double singleNormalPrice;
    private double singleSpecialPrice;
    private int quantityNormalPrice;
    private int quantitySpecialPrice;

    /**
     * Public no-arg constructor required by Jackson for deserialization.
     */
    public MultiPricedPromoDetailsDto() {
    }

    /**
     * Parameterized constructor with @JsonCreator for Jackson deserialization.
     */
    @JsonCreator
    public MultiPricedPromoDetailsDto(
            @JsonProperty("singleNormalPrice") double singleNormalPrice,
            @JsonProperty("singleSpecialPrice") double singleSpecialPrice,
            @JsonProperty("quantityNormalPrice") int quantityNormalPrice,
            @JsonProperty("quantitySpecialPrice") int quantitySpecialPrice) {
        this.singleNormalPrice = singleNormalPrice;
        this.singleSpecialPrice = singleSpecialPrice;
        this.quantityNormalPrice = quantityNormalPrice;
        this.quantitySpecialPrice = quantitySpecialPrice;
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

    @Override
    public String toString() {
        return "MultiPricedPromoDetailsDto{" +
                "singleNormalPrice=" + singleNormalPrice +
                ", singleSpecialPrice=" + singleSpecialPrice +
                ", quantityNormalPrice=" + quantityNormalPrice +
                ", quantitySpecialPrice=" + quantitySpecialPrice +
                '}';
    }

    /**
     * Builder class for constructing MultiPricedPromoDetailsDto instances.
     */
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
            return new MultiPricedPromoDetailsDto(this.singleNormalPrice, this.singleSpecialPrice,
                    this.quantityNormalPrice, this.quantitySpecialPrice);
        }
    }
}
