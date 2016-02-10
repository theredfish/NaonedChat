package naoned.sil.lp.naonedchat.components.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.jivesoftware.smack.packet.Message;
import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.bean.Chat;
import naoned.sil.lp.naonedchat.service.Connection;

/**
 * Created by ACHP on 23/01/2016.
 */
public class ChatFragment extends Fragment {

    public EditText message;
    public ListView listViewMessage;
    public ViewGroup rootView;
    public Button sendMessage;



    /**
     * Fonction appelé a la création de la vue.
     * Quand on affiche la vue on va charger les messages dans la listview.
     * A cette étape le singleton CHAT doit déja avoir été créé.
     *
     *  ALGO : on initialise les différents composants :
     *      -   Le button d'envoie de message,
     *      -   Le textfield d'envoie de message
     *      -   La listview contenant les messages.
     *      -   Une rootView (?)
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.chat, container, false);


        message = (EditText) rootView.findViewById(R.id.myMessage);
        listViewMessage = (ListView)rootView.findViewById(R.id.listView);
        sendMessage = (Button) rootView.findViewById(R.id.sendMessage);

        Chat chat = Chat.getInstance();

        // on peut recréer un adapter a chaque fois que l'on appelle la vue, ce n'est pas tres optimisé,
        //Mais pas tres dérangeant
        final ChatAdapter chatAdapter = new ChatAdapter(this.getContext(),
                R.layout.row_chat_left,
                chat.getUser().getConversation()
        );
        listViewMessage.setAdapter(chatAdapter);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Message m = new Message();
                m.setBody(message.getText().toString());
                m.setFrom(Connection.getInstance().getConnection().getUser());
                m.setTo(Chat.getInstance().getUser().getJID());
                Chat.getInstance().sendMessage(m);
                chatAdapter.notifyDataSetChanged();

            }
        });

        return rootView;

    }
}
