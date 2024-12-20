package com.component.checkout.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "DECIMAL(10, 2)")
    private double normalPrice;

    private int requiredQuantityForSpecialPrice;

    @Column(columnDefinition = "DECIMAL(10, 2)")
    private double specialPrice;

    @OneToMany(mappedBy = "primaryItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<BundleDiscount> bundleDiscounts;

    public Item() {
    }

    public Item(String name, double normalPrice, int requiredQuantityForSpecialPrice, double specialPrice) {
        this.name = name;
        this.normalPrice = normalPrice;
        this.requiredQuantityForSpecialPrice = requiredQuantityForSpecialPrice;
        this.specialPrice = specialPrice;
    }

    private Item(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.normalPrice = builder.normalPrice;
        this.requiredQuantityForSpecialPrice = builder.requiredQuantityForSpecialPrice;
        this.specialPrice = builder.specialPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getNormalPrice() {
        return normalPrice;
    }

    public Set<BundleDiscount> getBundleDiscounts() {
        return bundleDiscounts;
    }

    public int getRequiredQuantityForSpecialPrice() {
        return requiredQuantityForSpecialPrice;
    }

    public double getSpecialPrice() {
        return specialPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNormalPrice(double normalPrice) {
        this.normalPrice = normalPrice;
    }

    public void setRequiredQuantityForSpecialPrice(int requiredQuantityForSpecialPrice) {
        this.requiredQuantityForSpecialPrice = requiredQuantityForSpecialPrice;
    }

    public void setSpecialPrice(double specialPrice) {
        this.specialPrice = specialPrice;
    }

    public void setBundleDiscounts(Set<BundleDiscount> bundleDiscounts) {
        this.bundleDiscounts = bundleDiscounts;
    }

    public static class Builder {
        private Long id;
        private String name;
        private double normalPrice;
        private int requiredQuantityForSpecialPrice;
        private double specialPrice;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withNormalPrice(double normalPrice) {
            this.normalPrice = normalPrice;
            return this;
        }

        public Builder withRequiredQuantityForSpecialPrice(int requiredQuantityForSpecialPrice) {
            this.requiredQuantityForSpecialPrice = requiredQuantityForSpecialPrice;
            return this;
        }

        public Builder withSpecialPrice(double specialPrice) {
            this.specialPrice = specialPrice;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", normalPrice=" + normalPrice +
                ", requiredQuantityForSpecialPrice=" + requiredQuantityForSpecialPrice +
                ", specialPrice=" + specialPrice +
                '}';
    }
}
