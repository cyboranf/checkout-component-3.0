package com.component.checkout.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    private Cart cart;

    @Column(nullable = false)
    private int quantity;

    @Column(columnDefinition = "DECIMAL(10, 2)", nullable = false)
    private double singleNormalPrice;

    @Column(nullable = false)
    private int quantityNormalPrice;

    @Column(columnDefinition = "DECIMAL(10, 2)", nullable = false)
    private double singleSpecialPrice;

    @Column(nullable = false)
    private int quantitySpecialPrice;

    @Transient
    private double bundleDiscount;

    @Transient
    private int bundleDiscountQuantity;

    public CartItem() {}

    private CartItem(Builder builder) {
        this.item = builder.item;
        this.cart = builder.cart;
        this.quantity = builder.quantity;
        this.singleNormalPrice = builder.singleNormalPrice;
        this.quantityNormalPrice = builder.quantityWithNormalPrice;
        this.singleSpecialPrice = builder.singleSpecialPrice;
        this.quantitySpecialPrice = builder.quantityWithFinalPrice;
        this.bundleDiscount = builder.bundleDiscount;
        this.bundleDiscountQuantity = builder.bundleDiscountQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSingleNormalPrice() {
        return singleNormalPrice;
    }

    public void setSingleNormalPrice(double singleNormalPrice) {
        this.singleNormalPrice = singleNormalPrice;
    }

    public int getQuantityNormalPrice() {
        return quantityNormalPrice;
    }

    public void setQuantityNormalPrice(int quantityNormalPrice) {
        this.quantityNormalPrice = quantityNormalPrice;
    }

    public double getSingleSpecialPrice() {
        return singleSpecialPrice;
    }

    public void setSingleSpecialPrice(double singleSpecialPrice) {
        this.singleSpecialPrice = singleSpecialPrice;
    }

    public int getQuantitySpecialPrice() {
        return quantitySpecialPrice;
    }

    public void setQuantitySpecialPrice(int quantitySpecialPrice) {
        this.quantitySpecialPrice = quantitySpecialPrice;
    }

    public double getBundleDiscount() {
        return bundleDiscount;
    }

    public void setBundleDiscount(double bundleDiscount) {
        this.bundleDiscount = bundleDiscount;
    }

    public int getBundleDiscountQuantity() {
        return bundleDiscountQuantity;
    }

    public void setBundleDiscountQuantity(int bundleDiscountQuantity) {
        this.bundleDiscountQuantity = bundleDiscountQuantity;
    }

    public static class Builder {
        private Item item;
        private Cart cart;
        private int quantity;
        private double singleNormalPrice;
        private double singleSpecialPrice;
        private int quantityWithNormalPrice;
        private int quantityWithFinalPrice;
        private double bundleDiscount;
        private int bundleDiscountQuantity;

        public Builder withItem(Item item) {
            this.item = item;
            return this;
        }

        public Builder withCart(Cart cart) {
            this.cart = cart;
            return this;
        }

        public Builder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder withSingleNormalPrice(double singleNormalPrice) {
            this.singleNormalPrice = singleNormalPrice;
            return this;
        }

        public Builder withSingleSpecialPrice(double singleSpecialPrice) {
            this.singleSpecialPrice = singleSpecialPrice;
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

        public Builder withBundleDiscount(double bundleDiscount) {
            this.bundleDiscount = bundleDiscount;
            return this;
        }

        public Builder withBundleDiscountQuantity(int bundleDiscountQuantity) {
            this.bundleDiscountQuantity = bundleDiscountQuantity;
            return this;
        }

        public CartItem build() {
            return new CartItem(this);
        }
    }
}