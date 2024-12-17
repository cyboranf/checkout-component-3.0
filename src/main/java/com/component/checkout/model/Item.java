package com.component.checkout.model;

import jakarta.persistence.*;

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getNormalPrice() {
        return normalPrice;
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

    public void setRequiredQuantityForSpecialPrice(int specialQuantity) {
        this.requiredQuantityForSpecialPrice = specialQuantity;
    }

    public void setSpecialPrice(double specialPrice) {
        this.specialPrice = specialPrice;
    }

    public Item() {
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
