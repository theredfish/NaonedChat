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
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.Util.UserUtil;
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

    private final static int REQUEST_IMAGE_CAPTURE = 1;
    private static File picture;
    private Chat chat = Chat.getInstance();

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
        final ImageButton takePicture = (ImageButton) rootView.findViewById(R.id.takePicture);



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
                sendPicture(picture, picture.getName(), chat.getUser().getFullyQualifiedJID());
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


    protected void sendPicture(final File file, final String filename, String recipientJid) throws SmackException {
        // The file transfer manager


        new Thread(new Runnable() {
            @Override
            public void run() {
                FileTransferManager manager = FileTransferManager.getInstanceFor(Connection.getInstance().getConnection());
                // The outgoing file transfer (to send picture)

                OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer("test2@vps94220.ovh.net/spark");
                // Send the file
                try {
                    transfer.sendFile(file, filename);
                } catch (SmackException e) {
                    e.printStackTrace();
                }

                while(!transfer.isDone()) {
                    if(transfer.getStatus().equals(FileTransfer.Status.error)) {
                        System.out.println("ERROR!!! " + transfer.getError());
                    } else {
                        System.out.println(transfer.getStatus());
                        System.out.println(transfer.getProgress());
                    }
                    try {
                        wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).run();


    }
}
