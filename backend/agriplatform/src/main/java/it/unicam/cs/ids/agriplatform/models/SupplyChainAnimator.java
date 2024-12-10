package it.unicam.cs.ids.agriplatform.models;

public class SupplyChainAnimator extends User{
    public SupplyChainAnimator(long id, String username, String email, String password) {
        super(id, username, email, password, Role.PRODUCER);
    }
}
