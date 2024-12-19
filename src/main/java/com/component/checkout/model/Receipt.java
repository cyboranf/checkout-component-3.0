package com.component.checkout.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String issuedAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "receipt_purchased_items",
            joinColumns = @JoinColumn(name = "receipt_id"),
            inverseJoinColumns = @JoinColumn(name = "cart_item_id")
    )
    private List<CartItem> purchasedItems;

    @Column(columnDefinition = "DECIMAL(10, 2)", nullable = false)
    private double totalAmount;

    public Receipt() {
    }

    private Receipt(Builder builder) {
        this.id = builder.id;
        this.issuedAt = builder.issuedAt;
        this.purchasedItems = builder.purchasedItems;
        this.totalAmount = builder.totalAmount;
    }

    public Long getId() {
        return id;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public List<CartItem> getPurchasedItems() {
        return purchasedItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public void setPurchasedItems(List<CartItem> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public static class Builder {
        private Long id;
        private String issuedAt;
        private List<CartItem> purchasedItems;
        private double totalAmount;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withIssuedAt(String issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public Builder withPurchasedItems(List<CartItem> purchasedItems) {
            this.purchasedItems = purchasedItems;
            return this;
        }

        public Builder withTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Receipt build() {
            return new Receipt(this);
        }
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", issuedAt=" + issuedAt +
                ", purchasedItems=" + purchasedItems +
                ", totalAmount=" + totalAmount +
                '}';
    }
}