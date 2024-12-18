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
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference
    private Cart cart;

    @Column(nullable = false)
    private int quantity;

    public CartItem() {
    }

    private CartItem(Builder builder) {
        this.id = builder.id;
        this.item = builder.item;
        this.cart = builder.cart;
        this.quantity = builder.quantity;
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

    public static class Builder {
        private Long id;
        private Item item;
        private Cart cart;
        private int quantity;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

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

        public CartItem build() {
            return new CartItem(this);
        }
    }
}
