package it.unicam.cs.ids.agriplatform.models;
import jakarta.persistence.*;

@Entity
@Table(name = "product_details")

public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean isApproved;

    @Column
    private long userId;
    
    @Column
    private long productId;

    public ProductDetails(long id, String name, double price, String description, boolean isApproved, Long userId, Long productId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.productId = productId;
        this.description = description;
        this.isApproved = isApproved;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
