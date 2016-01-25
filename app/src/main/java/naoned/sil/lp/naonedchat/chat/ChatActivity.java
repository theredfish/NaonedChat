package naoned.sil.lp.naonedchat.chat;

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
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import naoned.sil.lp.naonedchat.R;
import service.Connection;

/**
 * Created by ACHP on 23/01/2016.
 */
public class ChatActivity extends Fragment {


    public EditText message;

    public ArrayList<Message> listMessages;
    public ListView listViewMessage;
    public chatAdapter chatAdapter;
    public ViewGroup rootView;
    private Queue<VCard> lastContacts = new LinkedList<>();


    private String currentUser;
    private HashMap<String,List<Message>> messagesList = new HashMap<>();

    public void addMessage(String username, Message message){

        refrechLastContactQueue(username);
        if(!messagesList.containsKey(username)) {
            this.messagesList.put(username, new ArrayList<Message>());
        }
        messagesList.get(username).add(message);
        if(username==currentUser && listViewMessage!=null){
            Log.d("refresh", "refresh view");
            refreshView();
        }


    }

    public Queue<VCard> getLastContactsQueue(){
        return this.lastContacts;
    }
    private void refrechLastContactQueue(String username){
        if(lastContacts.size()>=5){
            lastContacts.poll();
        }
        lastContacts.offer(Connection.getInstance().getVcard(username));
        Log.d("LAST CONTACTS", lastContacts.toString());
    }
    private void refreshView() {
        listViewMessage.setAdapter(
                new chatAdapter(rootView.getContext(),
                        R.layout.row_chat_left,
                        messagesList.get(currentUser).toArray(new Message[messagesList.get(currentUser).size()])
                )
        );

    }

    public void setUser(String user) {
        this.currentUser = user;
    }

    public String getUser() {
        return this.currentUser;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.chat, container, false);

        listMessages = new ArrayList<>();
        message = (EditText) rootView.findViewById(R.id.myMessage);
        listViewMessage = (ListView)rootView.findViewById(R.id.listView);
        Button sendMessage = (Button) rootView.findViewById(R.id.sendMessage);

        refreshView();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message m = new Message();
                m.setBody(message.getText().toString());
                m.setFrom(Connection.getInstance().getConnection().getUser());
                Connection.getInstance().sendMessage("test2@naonedchat", m);
                addMessage(m.getTo(), m);


            }
        });

        return rootView;
    }


}
