package it.unicam.cs.ids.agriplatform.models;

public class ProductProcessor extends User{
    public ProductProcessor(long id, String username, String email, String password) {
        super(id, username, email, password, Role.PRODUCER);
    }
}
