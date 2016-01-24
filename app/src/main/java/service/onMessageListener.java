package service;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by ACHP on 24/01/2016.
 */
public interface onMessageListener {
    public void onNewMessage(Message message);
}
