package naoned.sil.lp.naonedchat.components.chat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.Util.UserUtil;
import naoned.sil.lp.naonedchat.service.Connection;

/**
 * Created by ACHP on 23/01/2016.
 */
public class ChatFragment extends Fragment {

    public EditText message;
    public ArrayList<Message> listMessages;
    public ListView listViewMessage;
    public ChatAdapter chatAdapter;
    public ViewGroup rootView;

    private Queue<VCard> lastContacts = new LinkedList<>();
    private String currentUser;
    private HashMap<String,List<Message>> messagesList = new HashMap<>();

    private final static int REQUEST_IMAGE_CAPTURE = 1;

    public void addMessage(String username, Message message) {
        username = UserUtil.cleanUserJid(username);
        refreshLastContactQueue(username);

        if (!messagesList.containsKey(username)) {
            this.messagesList.put(username, new ArrayList<Message>());
        }

        messagesList.get(username).add(message);

        if (username == currentUser && listViewMessage != null) {
            refreshView();
        }
    }

    public Queue<VCard> getLastContactsQueue(){
        return this.lastContacts;
    }

    private void refreshLastContactQueue(String username){
        //Si le user est déja la queue, il faut le faire remonter, pour ça on le supprime de la linkedMist
        //Dans tous les cas on le place/replace ensuite en premiere position.
        username = UserUtil.cleanUserJid(username);
        for(VCard vcard: lastContacts){
            if(username.equals(UserUtil.cleanUserJid(vcard.getFrom()))){
                lastContacts.remove(vcard);
                break;
            }
        }
        if (lastContacts.size()>=5) {
            lastContacts.poll();
        }
        lastContacts.offer(Connection.getInstance().getVcard(username));

    }

    private void refreshView() {
        if(rootView!=null){
            listViewMessage.setAdapter(
                    new ChatAdapter(rootView.getContext(),
                            R.layout.row_chat_left,
                            messagesList.get(currentUser).toArray(new Message[messagesList.get(currentUser).size()])
                    )
            );
        }
    }

    public void setUser(String user) {
        this.currentUser = UserUtil.cleanUserJid(user);
        refreshView();
    }

    public String getUser() {
        return this.currentUser;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.chat, container, false);
        listMessages = new ArrayList<>();
        message = (EditText) rootView.findViewById(R.id.myMessage);
        listViewMessage = (ListView)rootView.findViewById(R.id.listView);
        Button sendMessage = (Button) rootView.findViewById(R.id.sendMessage);
        final ImageButton takePicture = (ImageButton) rootView.findViewById(R.id.takePicture);

        refreshView();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Message m = new Message();
                m.setBody(message.getText().toString());
                m.setFrom(Connection.getInstance().getConnection().getUser());
                Connection.getInstance().sendMessage(currentUser, m);
                addMessage(m.getTo(), m);
            }
        });

        takePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePicture.setBackgroundResource(R.drawable.ic_action_camera_active);
                dispatchTakePictureIntent();
            }
        });

        return rootView;
    }

    /**
     * Invoke take picture intent.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(rootView.getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
