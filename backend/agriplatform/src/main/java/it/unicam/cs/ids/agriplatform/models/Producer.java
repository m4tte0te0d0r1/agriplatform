package it.unicam.cs.ids.agriplatform.models;

public class Producer extends User{
        public Producer(long id, String username, String email, String password) {
            super(id, username, email, password, Role.PRODUCER);
        }
}
