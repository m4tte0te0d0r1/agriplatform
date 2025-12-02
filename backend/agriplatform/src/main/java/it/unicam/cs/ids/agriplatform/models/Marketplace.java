package it.unicam.cs.ids.agriplatform.models;

import jakarta.persistence.*;

@Entity
@Table(name = "marketplace")
public class Marketplace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MarketplaceItemType itemType; // PRODUCT or PACKAGE

    @Column(nullable = false)
    private long itemId; // ID of product or package

    @Column(nullable = false)
    private long sellerId;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column
    private double price;

    public Marketplace() {
    }

    public Marketplace(long id, MarketplaceItemType itemType, long itemId, long sellerId, boolean isAvailable,
            double price) {
        this.id = id;
        this.itemType = itemType;
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.isAvailable = isAvailable;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MarketplaceItemType getItemType() {
        return itemType;
    }

    public void setItemType(MarketplaceItemType itemType) {
        this.itemType = itemType;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
