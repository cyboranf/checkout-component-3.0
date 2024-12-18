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

    public CartItem() {}

    private CartItem(Builder builder) {
        this.item = builder.item;
        this.cart = builder.cart;
        this.quantity = builder.quantity;
        this.singleNormalPrice = builder.singleNormalPrice;
        this.quantityNormalPrice = builder.quantityWithNormalPrice;
        this.singleSpecialPrice = builder.singleSpecialPrice;
        this.quantitySpecialPrice = builder.quantityWithFinalPrice;
    }

    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public Cart getCart() {
        return cart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
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
        private Item item;
        private Cart cart;
        private int quantity;
        private double singleNormalPrice;
        private double singleSpecialPrice;
        private int quantityWithNormalPrice;
        private int quantityWithFinalPrice;

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

        public CartItem build() {
            return new CartItem(this);
        }
    }
}
