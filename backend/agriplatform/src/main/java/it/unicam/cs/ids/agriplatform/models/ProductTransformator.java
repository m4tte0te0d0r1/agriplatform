package it.unicam.cs.ids.agriplatform.models;

public class ProductTransformator extends User{
    public ProductTransformator(long id, String username, String email, String password) {
        super(id, username, email, password, Role.PRODUCTS_TRANSFORMATOR);
    }
}
