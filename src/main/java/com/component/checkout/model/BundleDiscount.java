package com.component.checkout.model;

import jakarta.persistence.*;

@Entity
public class BundleDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "primary_item_id", nullable = false)
    private Item primaryItem;

    @ManyToOne
    @JoinColumn(name = "bundled_item_id", nullable = false)
    private Item bundledItem;

    @Column(columnDefinition = "DECIMAL(10, 2)")
    private double discount;

    public BundleDiscount() {}

    public BundleDiscount(Item primaryItem, Item bundledItem, double discount) {
        this.primaryItem = primaryItem;
        this.bundledItem = bundledItem;
        this.discount = discount;
    }

    public Item getPrimaryItem() {
        return primaryItem;
    }

    public void setPrimaryItem(Item primaryItem) {
        this.primaryItem = primaryItem;
    }

    public Item getBundledItem() {
        return bundledItem;
    }

    public void setBundledItem(Item bundledItem) {
        this.bundledItem = bundledItem;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}