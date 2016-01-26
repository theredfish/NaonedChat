package naoned.sil.lp.naonedchat.listeners.chat;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by ACHP on 24/01/2016.
 */
public interface MessageListener {
    public void onNewMessage(Message message);
}
