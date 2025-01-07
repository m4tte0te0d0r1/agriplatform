package it.unicam.cs.ids.agriplatform.models;
import jakarta.persistence.*;

@Entity
@Table(name = "product_packages")
public class ProductPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean isApproved;

    @Column
    private long userId;

    @Column
    private String description;

    public ProductPackage(long id, String name, double price, long userId, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.userId = userId;
        this.description = description;
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
