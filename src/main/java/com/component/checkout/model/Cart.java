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
    private double totalPriceWithDiscounts = 0.0;

    @Column(columnDefinition = "DECIMAL(10, 2)", nullable = false)
    private double totalPriceWithoutDiscounts = 0.0;

    @Column(columnDefinition = "DECIMAL(10, 2)", nullable = false)
    private double sumOfDiscount = 0.0;

    @Column(nullable = false)
    private int totalBundlePromoQuantity = 0;

    @Column(columnDefinition = "DECIMAL(10, 2)", nullable = false)
    private double totalBundleDiscount = 0.0;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Cart() {}

    private Cart(Builder builder) {
        this.id = builder.id;
        this.cartItems = builder.cartItems;
        this.totalPriceWithDiscounts = builder.totalPriceWithDiscounts;
        this.totalPriceWithoutDiscounts = builder.totalPriceWithoutDiscounts;
        this.sumOfDiscount = builder.sumOfDiscount;
        this.totalBundlePromoQuantity = builder.totalBundlePromoQuantity;
        this.totalBundleDiscount = builder.totalBundleDiscount;
        this.user = builder.user;
    }

    public Long getId() {
        return id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPriceWithDiscounts() {
        return totalPriceWithDiscounts;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void setTotalPriceWithDiscounts(double totalPriceWithDiscounts) {
        this.totalPriceWithDiscounts = totalPriceWithDiscounts;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getTotalPriceWithoutDiscounts() {
        return totalPriceWithoutDiscounts;
    }

    public void setTotalPriceWithoutDiscounts(double totalPriceWithoutDiscounts) {
        this.totalPriceWithoutDiscounts = totalPriceWithoutDiscounts;
    }

    public double getSumOfDiscount() {
        return sumOfDiscount;
    }

    public void setSumOfDiscount(double sumOfDiscount) {
        this.sumOfDiscount = sumOfDiscount;
    }

    public int getTotalBundlePromoQuantity() {
        return totalBundlePromoQuantity;
    }

    public void setTotalBundlePromoQuantity(int totalBundlePromoQuantity) {
        this.totalBundlePromoQuantity = totalBundlePromoQuantity;
    }

    public double getTotalBundleDiscount() {
        return totalBundleDiscount;
    }

    public void setTotalBundleDiscount(double totalBundleDiscount) {
        this.totalBundleDiscount = totalBundleDiscount;
    }

    public static class Builder {
        private Long id;
        private List<CartItem> cartItems;
        private double totalPriceWithDiscounts;
        private double totalPriceWithoutDiscounts;
        private double sumOfDiscount;
        private int totalBundlePromoQuantity;
        private double totalBundleDiscount;
        private User user;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCartItems(List<CartItem> cartItems) {
            this.cartItems = cartItems;
            return this;
        }

        public Builder withTotalPriceWithDiscounts(double totalPriceWithDiscounts) {
            this.totalPriceWithDiscounts = totalPriceWithDiscounts;
            return this;
        }

        public Builder withTotalPriceWithoutDiscounts(double totalPriceWithoutDiscounts) {
            this.totalPriceWithoutDiscounts = totalPriceWithoutDiscounts;
            return this;
        }

        public Builder withSumOfDiscount(double sumOfDiscount) {
            this.sumOfDiscount = sumOfDiscount;
            return this;
        }

        public Builder withTotalBundlePromoQuantity(int totalBundlePromoQuantity) {
            this.totalBundlePromoQuantity = totalBundlePromoQuantity;
            return this;
        }

        public Builder withTotalBundleDiscount(double totalBundleDiscount) {
            this.totalBundleDiscount = totalBundleDiscount;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Cart build() {
            return new Cart(this);
        }
    }
}
