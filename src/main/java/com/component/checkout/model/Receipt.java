package com.component.checkout.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date issuedAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "receipt_purchased_items",
            joinColumns = @JoinColumn(name = "receipt_id"),
            inverseJoinColumns = @JoinColumn(name = "cart_item_id")
    )
    private List<CartItem> purchasedItems;

    @Column(columnDefinition = "DECIMAL(10, 2)", nullable = false)
    private double totalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public List<CartItem> getPurchasedItems() {
        return purchasedItems;
    }

    public void setPurchasedItems(List<CartItem> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
