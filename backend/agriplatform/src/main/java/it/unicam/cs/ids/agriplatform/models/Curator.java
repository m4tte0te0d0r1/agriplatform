package it.unicam.cs.ids.agriplatform.models;

public class Curator extends User{
    public Curator(long id, String username, String email, String password) {
        super(id, username, email, password, Role.CURATOR);
    }
}
