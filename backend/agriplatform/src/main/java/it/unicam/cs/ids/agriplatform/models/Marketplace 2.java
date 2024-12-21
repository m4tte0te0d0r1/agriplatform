package it.unicam.cs.ids.agriplatform.models;

import jakarta.persistence.*;

@Entity
@Table(name = "marketplace")
public class Marketplace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long productId;

    @Column(nullable = false)
    private long productParentId;

    @Column(nullable = false)
    private long sellerId;

    @Column(nullable = false)
    private long purchaserId;

    public Marketplace(long id, long productId, long productParentId, long sellerId, long purchaserId) {
        this.id = id;
        this.productId = productId;
        this.productParentId = productParentId;
        this.sellerId = sellerId;
        this.purchaserId = purchaserId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getProductParentId() {
        return productParentId;
    }

    public void setProductParentId(long productParentId) {
        this.productParentId = productParentId;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public long getPurchaserId() {
        return purchaserId;
    }

    public void setPurchaserId(long purchaserId) {
        this.purchaserId = purchaserId;
    }
}
