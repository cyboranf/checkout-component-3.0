package com.component.checkout.presentation.dto.receipt;

import com.component.checkout.presentation.dto.promotion.ItemDiscountDto;

public class PurchasedItemDto {

    private String itemName;
    private int quantity;
    private double pricePerOneItem;
    private double priceBeforeDiscounts;
    private double priceWithDiscounts;
    private ItemDiscountDto itemDiscount;

    public PurchasedItemDto() {
    }

    private PurchasedItemDto(Builder builder) {
        this.itemName = builder.itemName;
        this.quantity = builder.quantity;
        this.pricePerOneItem = builder.pricePerOneItem;
        this.priceBeforeDiscounts = builder.priceBeforeDiscounts;
        this.priceWithDiscounts = builder.priceWithDiscounts;
        this.itemDiscount = builder.itemDiscount;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPricePerOneItem(double pricePerOneItem) {
        this.pricePerOneItem = pricePerOneItem;
    }

    public void setItemDiscount(ItemDiscountDto itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public void setPriceWithDiscounts(double priceWithDiscounts) {
        this.priceWithDiscounts = priceWithDiscounts;
    }

    public void setPriceBeforeDiscounts(double priceBeforeDiscounts) {
        this.priceBeforeDiscounts = priceBeforeDiscounts;
    }

    public String getItemName() {
        return itemName;
    }


    public int getQuantity() {
        return quantity;
    }


    public double getPricePerOneItem() {
        return pricePerOneItem;
    }


    public double getPriceBeforeDiscounts() {
        return priceBeforeDiscounts;
    }


    public double getPriceWithDiscounts() {
        return priceWithDiscounts;
    }


    public ItemDiscountDto getItemDiscount() {
        return itemDiscount;
    }


    public static class Builder {

        private String itemName;
        private int quantity;
        private double pricePerOneItem;
        private double priceBeforeDiscounts;
        private double priceWithDiscounts;
        private ItemDiscountDto itemDiscount;

        public Builder withItemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public Builder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder withPricePerOneItem(double pricePerOneItem) {
            this.pricePerOneItem = pricePerOneItem;
            return this;
        }

        public Builder withPriceBeforeDiscounts(double priceBeforeDiscounts) {
            this.priceBeforeDiscounts = priceBeforeDiscounts;
            return this;
        }

        public Builder withPriceWithDiscounts(double priceWithDiscounts) {
            this.priceWithDiscounts = priceWithDiscounts;
            return this;
        }

        public Builder withItemDiscount(ItemDiscountDto itemDiscount) {
            this.itemDiscount = itemDiscount;
            return this;
        }

        public PurchasedItemDto build() {
            return new PurchasedItemDto(this);
        }
    }

    @Override
    public String toString() {
        return "PurchasedItemDto{" +
                "itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", pricePerOneItem=" + pricePerOneItem +
                ", priceBeforeDiscounts=" + priceBeforeDiscounts +
                ", priceWithDiscounts=" + priceWithDiscounts +
                ", itemDiscount=" + itemDiscount +
                '}';
    }
}
