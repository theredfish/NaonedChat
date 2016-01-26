package naoned.sil.lp.naonedchat.components.contacts;

/**
 * Created by julian on 22/01/16.
 */
public class Contact {

    private String pseudo;
    private String email;
    private String picture;

    public Contact(String pseudo, String email, String picture) {
        this.pseudo = pseudo;
        this.email = email;
        this.picture = picture;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getPicture() {
        return picture;
    }

    public String getEmail() {
        return email;
    }
}
