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
    private long productPackageId;

    @Column(nullable = false)
    private long sellerId;

    @Column(nullable = false)
    private long purchaserId;

    public Marketplace(long id, long productId, long productPackageId, long sellerId, long purchaserId) {
        this.id = id;
        this.productId = productId;
        this.productPackageId = productPackageId;
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

    public long getProductPackageId() {
        return productPackageId;
    }

    public void setProductPackageId(long productPackageId) {
        this.productPackageId = productPackageId;
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
