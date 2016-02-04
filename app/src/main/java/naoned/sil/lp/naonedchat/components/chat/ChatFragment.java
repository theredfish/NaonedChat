package naoned.sil.lp.naonedchat.components.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Date;
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
    public ViewGroup rootView;

    private Queue<VCard> lastContacts = new LinkedList<>();
    private String currentUser;
    private HashMap<String,List<Message>> messagesList = new HashMap<>();

    private final static int REQUEST_IMAGE_CAPTURE = 1;
    private static File picture;

    public void addMessage(String username, Message message) {
        username = UserUtil.cleanUserJid(username);
        refreshLastContactQueue(username);

        if (!messagesList.containsKey(username)) {
            this.messagesList.put(username, new ArrayList<Message>());
        }

        messagesList.get(username).add(message);

        Log.e("userN == getCuUsr", "== ? " + (username.equals(getCurrentUser())) + " username = " + username + " and getCurrentUser = " + getCurrentUser());
        if (username.equals(getCurrentUser()) && listViewMessage != null) {
            Log.e("refresh", "refresh view should be occu");
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
                            messagesList.get(getCurrentUser()).toArray(new Message[messagesList.get(getCurrentUser()).size()])
                    )
            );
        }
    }

    public void setUser(String user) {
        this.currentUser = user;
        refreshView();
    }

    /**
     * Sometimes we don't need to get fully qualified JID.
     * For example foo.bar@naonedchat/Spark becomes foo.bar@naonedchat
     *
     * @return user's JID which is not fully qualified
     */
    public String getCurrentUser() {
        return UserUtil.cleanUserJid(currentUser);
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
                Connection.getInstance().sendMessage(getCurrentUser(), m);
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
     *
     * See http://developer.android.com/training/camera/photobasics.html
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check if there is a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(rootView.getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            picture = null;

            try {
                picture = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(rootView.getContext(), "Error occurs, please check your storage.", Toast.LENGTH_LONG).show();
            }

            // Continue only if the File was successfully created
            if (picture != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Handle take picture intent result.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                sendPicture(picture, picture.getName(), currentUser);
                Toast.makeText(rootView.getContext(), "Picture sent!", Toast.LENGTH_LONG).show();
            } catch (SmackException ex) {
                Toast.makeText(rootView.getContext(), "Error while sending the photo. Please try again.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode != REQUEST_IMAGE_CAPTURE) {
            Toast.makeText(rootView.getContext(), "Error during recovery of the photo. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Create and initialize new File object.
     * Here we create image file by setting specific suffix extension.
     *
     * See Android doc : http://developer.android.com/training/camera/photobasics.html
     *
     * @throws IOException
     */
    protected File createImageFile() throws IOException {
        // Get unique filename with timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    /**
     * Send bitmap picture to the specified recipient.
     * See doc : http://www.igniterealtime.org/builds/smack/docs/latest/documentation/extensions/filetransfer.html
     *
     * @param file the file to send
     * @param filename the file name or the file description
     * @param recipientJid which would be fully qualified (example foo.bar@naonedchat/Spark)
     */
    protected void sendPicture(File file, String filename, String recipientJid) throws SmackException {
        // The file transfer manager
        FileTransferManager manager = FileTransferManager.getInstanceFor(Connection.getInstance().getConnection());
        // The outgoing file transfer (to send picture)
        OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(recipientJid);
        // Send the file
        transfer.sendFile(file, filename);
    }
}
