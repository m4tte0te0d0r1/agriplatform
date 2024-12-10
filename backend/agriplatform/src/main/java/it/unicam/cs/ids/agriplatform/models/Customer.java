package it.unicam.cs.ids.agriplatform.models;

public class Customer extends User{
    public Customer(long id, String username, String email, String password) {
        super(id, username, email, password, Role.PRODUCER);
    }
}
