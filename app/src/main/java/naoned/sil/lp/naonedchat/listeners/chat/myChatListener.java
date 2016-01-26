package naoned.sil.lp.naonedchat.listeners.chat;

import android.util.Log;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by ACHP on 24/01/2016.
 */
public class myChatListener implements ChatMessageListener {
private onMessageListener myMessageListener;

    public myChatListener(onMessageListener myMessageListener) {
        this.myMessageListener = myMessageListener;
    }


    @Override
    public void processMessage(Chat chat, Message message) {

        if (message.getType() == Message.Type.chat || message.getType() == Message.Type.normal) {

            if (message.getBody() != null) {
                Log.d("HEY", " hey j'ai un message");
                if(this.myMessageListener!=null){
                    myMessageListener.onNewMessage(message);
                }

                               /* context.runOnUiThread(new Runnable() {

                                                          @Override
                                                          public void run() {
                                                              context.listMessages.add(message);
                                                              ListView l = (ListView) context.findViewById(R.id.listView);
                                                              l.setAdapter(new ChatAdapter(context,
                                                                      R.layout.row_chat_left,
                                                                      context.listMessages.toArray(new Message[context.listMessages.size()])));
                                                          }
                                                      }
                                );*/

            }
        }
    }
}
