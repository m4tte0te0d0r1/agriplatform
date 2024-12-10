package it.unicam.cs.ids.agriplatform.models;

public class GenericUser extends User{
    public GenericUser(long id, String username, String email, String password) {
        super(id, username, email, password, Role.PRODUCER);
    }
}
