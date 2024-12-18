package com.component.checkout.presentation.dto.receipt;

import com.component.checkout.presentation.dto.promotion.ItemDiscountDto;

public class PurchasedItemDto {

    private String itemName;
    private int quantity;
    private double pricePerOneItem;
    private double priceBeforeDiscounts;
    private double priceWithDiscounts;
    private ItemDiscountDto itemDiscount;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerOneItem() {
        return pricePerOneItem;
    }

    public void setPricePerOneItem(double pricePerOneItem) {
        this.pricePerOneItem = pricePerOneItem;
    }

    public double getPriceBeforeDiscounts() {
        return priceBeforeDiscounts;
    }

    public void setPriceBeforeDiscounts(double priceBeforeDiscounts) {
        this.priceBeforeDiscounts = priceBeforeDiscounts;
    }

    public double getPriceWithDiscounts() {
        return priceWithDiscounts;
    }

    public void setPriceWithDiscounts(double priceWithDiscounts) {
        this.priceWithDiscounts = priceWithDiscounts;
    }

    public ItemDiscountDto getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(ItemDiscountDto itemDiscount) {
        this.itemDiscount = itemDiscount;
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
            PurchasedItemDto purchasedItemDto = new PurchasedItemDto();
            purchasedItemDto.setItemName(this.itemName);
            purchasedItemDto.setQuantity(this.quantity);
            purchasedItemDto.setPricePerOneItem(this.pricePerOneItem);
            purchasedItemDto.setPriceBeforeDiscounts(this.priceBeforeDiscounts);
            purchasedItemDto.setPriceWithDiscounts(this.priceWithDiscounts);
            purchasedItemDto.setItemDiscount(this.itemDiscount);
            return purchasedItemDto;
        }
    }
}
