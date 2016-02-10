package naoned.sil.lp.naonedchat.bean;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.ArrayList;

import naoned.sil.lp.naonedchat.Util.UserUtil;

/**
 * Created by ACHP on 03/02/2016.
 */
public class User {

    private VCard vcard;
    private ArrayList<Message> conversation;
    private int unreadMessage;


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
    }

    public VCard getVCard() {
        return this.vcard;
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
}
