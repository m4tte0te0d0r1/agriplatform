package it.unicam.cs.ids.agriplatform.models;

public class ProductsDistributor extends User{
    public ProductsDistributor(long id, String username, String email, String password) {
        super(id, username, email, password, Role.PRODUCTS_DISTRIBUTOR);
    }
}
