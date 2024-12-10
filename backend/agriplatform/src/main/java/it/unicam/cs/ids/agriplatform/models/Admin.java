package it.unicam.cs.ids.agriplatform.models;

public class Admin extends User{
    public Admin(long id, String username, String email, String password) {
        super(id, username, email, password, Role.ADMIN);
    }
}
