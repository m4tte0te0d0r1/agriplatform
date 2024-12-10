package it.unicam.cs.ids.agriplatform.models;

public class SocialMediaCreator extends User{
    public SocialMediaCreator(long id, String username, String email, String password) {
        super(id, username, email, password, Role.SOCIAL_MEDIA_CREATOR);
    }
}
