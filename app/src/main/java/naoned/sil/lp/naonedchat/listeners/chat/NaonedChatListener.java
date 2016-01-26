package naoned.sil.lp.naonedchat.listeners.chat;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by ACHP on 24/01/2016.
 */
public class NaonedChatListener implements ChatMessageListener {
    private MessageListener messageListener;

    public NaonedChatListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void processMessage(Chat chat, Message message) {
        if (message.getType() == Message.Type.chat || message.getType() == Message.Type.normal) {

            if (message.getBody() != null) {
                if (this.messageListener != null) {
                    messageListener.onNewMessage(message);
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
