package service;

import android.util.Log;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;

/**
 * Created by ACHP on 24/01/2016.
 */
public class myChatManagerListener implements ChatManagerListener {
    onMessageListener myMessageListener;
    public myChatManagerListener(onMessageListener myMessageListener) {
        this.myMessageListener = myMessageListener;
    }

    @Override
    public void chatCreated(Chat chat, boolean b) {
        Log.d("CHAT", "CHAT CREATED");
        chat.addMessageListener(new myChatListener(myMessageListener));
    }
}
