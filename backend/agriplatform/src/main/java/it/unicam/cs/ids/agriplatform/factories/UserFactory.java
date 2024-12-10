package it.unicam.cs.ids.agriplatform.factories;

import it.unicam.cs.ids.agriplatform.models.*;

public class UserFactory {

    public static User createUser(Role role, long id, String name, String email, String password) {
        switch (role) {
            case PRODUCER:
                return new Producer(id, name, email, password);
            case PRODUCT_PROCESSOR:
                return new ProductProcessor(id, name, email, password);
            case TYPICAL_PRODUCTS_DISTRIBUTOR:
                return new TypicalProductsDistributor(id, name, email, password);
            case CURATOR:
                return new Curator(id, name, email, password);
            case SUPPLY_CHAIN_ANIMATOR:
                return new SupplyChainAnimator(id, name, email, password);
            case CUSTOMER:
                return new Customer(id, name, email, password);
            case GENERIC_USER:
                return new GenericUser(id, name, email, password);
            case ADMIN:
                return new Admin(id, name, email, password);
            case SOCIAL_MEDIA_CREATOR:
                return new SocialMediaCreator(id, name, email, password);
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
