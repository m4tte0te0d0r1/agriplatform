package it.unicam.cs.ids.agriplatform.models;

public class TypicalProductsDistributor extends User{
    public TypicalProductsDistributor(long id, String username, String email, String password) {
        super(id, username, email, password, Role.PRODUCER);
    }
}
