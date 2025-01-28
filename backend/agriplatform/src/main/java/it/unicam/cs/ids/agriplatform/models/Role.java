package it.unicam.cs.ids.agriplatform.models;

public enum Role {
    PRODUCER(1),
    PRODUCTS_TRANSFORMATOR(2), 
    PRODUCTS_DISTRIBUTOR(3),
    CURATOR(4),
    SUPPLY_CHAIN_ANIMATOR(5),
    CUSTOMER(6),
    GENERIC_USER(7),
    ADMIN(8),
    SOCIAL_MEDIA_CREATOR(9);

    private final int number;

    Role(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static Role fromNumber(int number) {
        for (Role role : Role.values()) {
            if (role.getNumber() == number) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role number: " + number);
    }
}
