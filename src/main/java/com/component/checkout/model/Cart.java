package com.component.checkout.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    @Column(columnDefinition = "DECIMAL(10, 2)", nullable = false)
    private double totalPrice;

    private Cart(Builder builder) {
        this.id = builder.id;
        this.cartItems = builder.cartItems;
        this.totalPrice = builder.totalPrice;
    }

    public Cart() {

    }

    public Long getId() {
        return id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static class Builder {
        private Long id;
        private List<CartItem> cartItems;
        private double totalPrice;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCartItems(List<CartItem> cartItems) {
            this.cartItems = cartItems;
            return this;
        }

        public Builder withTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Cart build() {
            return new Cart(this);
        }
    }
}