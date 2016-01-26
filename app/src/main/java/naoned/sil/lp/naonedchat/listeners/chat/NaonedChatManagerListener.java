package naoned.sil.lp.naonedchat.listeners.chat;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;

/**
 * Created by ACHP on 24/01/2016.
 */
public class NaonedChatManagerListener implements ChatManagerListener {
    MessageListener messageListener;

    public NaonedChatManagerListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void chatCreated(Chat chat, boolean b) {
        chat.addMessageListener(new NaonedChatListener(messageListener));
    }
}
