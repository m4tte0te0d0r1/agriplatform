package it.unicam.cs.ids.agriplatform.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private int quantity;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductDetail> details;

    @ManyToOne
    @JoinColumn(name = "certification_id")
    private Certification certification;

    @Column(nullable = false)
    private boolean available = true;

    @Column(nullable = false)
    private boolean approved = false;

    public Product() {
    }

    public Product(
            long id,
            String name,
            double price,
            long userId,
            int quantity,
            List<ProductDetail> details,
            Certification certification) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.userId = userId;
        this.quantity = quantity;
        this.details = details;
        this.certification = certification;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<ProductDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ProductDetail> details) {
        this.details = details;
    }

    public Certification getCertification() {
        return certification;
    }

    public void setCertification(Certification certification) {
        this.certification = certification;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
