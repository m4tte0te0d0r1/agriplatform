package it.unicam.cs.ids.agriplatform.models.roles;

import it.unicam.cs.ids.agriplatform.models.Role;
import it.unicam.cs.ids.agriplatform.models.User;

public class Admin extends User {
    public Admin(long id, String username, String email, String password) {
        super(id, username, email, password, Role.ADMIN);
    }
}
