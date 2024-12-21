package it.unicam.cs.ids.agriplatform.models.roles;

import it.unicam.cs.ids.agriplatform.models.Role;
import it.unicam.cs.ids.agriplatform.models.User;

public class Producer extends User{
        public Producer(long id, String username, String email, String password) {
            super(id, username, email, password, Role.PRODUCER);
        }
}
