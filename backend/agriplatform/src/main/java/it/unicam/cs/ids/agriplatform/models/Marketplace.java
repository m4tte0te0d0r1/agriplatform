package it.unicam.cs.ids.agriplatform.models;

import jakarta.persistence.*;

@Entity
@Table(name = "marketplace")
public class Marketplace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long productParentId;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private Long purchaserId;

    public Marketplace(Long id, Long productId, Long productParentId, Long sellerId, Long purchaserId) {
        this.id = id;
        this.productId = productId;
        this.productParentId = productParentId;
        this.sellerId = sellerId;
        this.purchaserId = purchaserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductParentId() {
        return productParentId;
    }

    public void setProductParentId(Long productParentId) {
        this.productParentId = productParentId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getPurchaserId() {
        return purchaserId;
    }

    public void setPurchaserId(Long purchaserId) {
        this.purchaserId = purchaserId;
    }
}
