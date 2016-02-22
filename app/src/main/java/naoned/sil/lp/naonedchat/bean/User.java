package naoned.sil.lp.naonedchat.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.Drawable;
import android.util.Log;

import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.Util.DrawableUtil;
import naoned.sil.lp.naonedchat.Util.UserUtil;
import naoned.sil.lp.naonedchat.components.lastContacts.ScreenSlideActivity;
import naoned.sil.lp.naonedchat.service.Connection;

/**
 * Created by ACHP on 03/02/2016.
 */
public class User {

    private VCard vcard;
    private ArrayList<Message> conversation;
    private HashMap<String,Bitmap> listPhoto;
    private int unreadMessage;
    private Bitmap avatar;

    public void newUnreadMessage() {
        this.unreadMessage++;
    }

    public int getUnreadMessage() {
        return unreadMessage;
    }

    public void markAsRead() {
        this.unreadMessage = 0;
    }

    public User(VCard vcard) {
        this.vcard = vcard;
        this.unreadMessage = 0;
        this.conversation = new ArrayList<>();
        this.listPhoto = new HashMap<>();
    }

    public VCard getVCard() {
        return (this.vcard == null ? null : this.vcard);
    }

    public void newPhoto(Bitmap bmp, String userFrom){
        String uuid = UUID.randomUUID().toString();
        Message m = new Message();
        m.setFrom(userFrom);
        m.setTo(UserUtil.cleanUserJid(Connection.getInstance().getConnection().getUser()));
        m.setBody(uuid);

        this.listPhoto.put(uuid, bmp);
        newMessage(m);
    }

    public HashMap<String, Bitmap> getPhotos(){
        return this.listPhoto;
    }
    public void newMessage(Message message) {
        this.conversation.add(message);
        //Initialisation du chat.
        if(!Chat.getInstance().isInitialized()){
            Chat.getInstance().init(this);
        }else{
            //Ajout du nouveau message dans le chat
            if(Chat.getInstance().getUser()==this){
                Chat.getInstance().addMessage(message);
            }else{
                newUnreadMessage();
            }
        }
    }

    public ArrayList<Message> getConversation() {
        return this.conversation;
    }

    public String getJID() {
        return UserUtil.cleanUserJid(this.vcard.getFrom());

    }

    public String getFullJID() {
        return this.vcard.getFrom();
    }
    /**
     * Get the user fully qualified jid. Usefull with file transfer.
     * Use current user's JID with roster presence.
     * Ex. john.snow@naonedchat.fr will become john.snow@naonedchat.fr/Spark
     *
     * @return the fully qualified JID
     */
    public String getFullyQualifiedJID() {
        Roster roster = Roster.getInstanceFor(Connection.getInstance().getConnection());
        Presence presence = roster.getPresence(this.getJID());

        return presence.getFrom();
    }

    /**
     * Get nickname for the current user.
     * Several problems can occur : by default nickname is null. But if for one reason the user is edited
     * (from another client such as spark or admin side), all fields are set from null to empty String.
     * Here we catch all these cases to prevent bugs.
     *
     * @return user's nickname if exists else full user's JID
     */
    public String getNickname() {
        String nickname = this.getVCard().getNickName();

        return ((nickname == null || nickname.equals("")) ? this.getFullJID() : nickname);
    }

    /**
     * Currently avatar can be null. Because we cannot access to drawable resources from class...
     * @return user's avatar
     */
    public Bitmap getAvatar() {
        return this.avatar;
    }

    /**
     * Load default avatar to user instance
     * @param avatar
     */
    public void setAvatar(byte[] avatar) {
        this.avatar = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
    }


}
