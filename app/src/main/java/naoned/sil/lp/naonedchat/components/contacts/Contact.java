package naoned.sil.lp.naonedchat.components.contacts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by julian on 22/01/16.
 */
public class Contact {

    private String pseudo;
    private String jid;
    private byte[] picture;

    public Contact(String pseudo, String jid, byte[] picture) {
        this.pseudo = pseudo;
        this.jid = jid;
        this.picture = picture;
    }

    public String getPseudo() {
        return pseudo;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] pictureArray) {
        this.picture = pictureArray;
    }

    /**
     * Get bitmap from current picture byte array.
     * Each contact has one default picture or specified picture.
     *
     * @return Bitmap bitmapPicture
     */
    public Bitmap getBitmapPicture() {
        Bitmap bitmapPicture = BitmapFactory.decodeByteArray(getPicture(), 0, getPicture().length);

        return bitmapPicture;
    }

    public String getJid() {
        return jid;
    }
}
