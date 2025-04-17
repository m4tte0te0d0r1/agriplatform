package it.unicam.cs.ids.agriplatform.models;

import jakarta.persistence.*;

public class Certification {

    @Column(nullable = false)
    private boolean isApproved;

    public Certification() {
    }

    public Certification(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
