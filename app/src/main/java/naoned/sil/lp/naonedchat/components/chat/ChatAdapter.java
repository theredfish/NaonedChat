package naoned.sil.lp.naonedchat.components.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.Util.UserUtil;
import naoned.sil.lp.naonedchat.bean.ContactList;
import naoned.sil.lp.naonedchat.bean.User;
import naoned.sil.lp.naonedchat.service.Connection;

/**
 * Created by ACHP on 23/01/2016.
 */
public class ChatAdapter extends ArrayAdapter<Message> {
   /* public ChatAdapter(Context context, int resource, Message[] messageList) {
        super(context, resource, messageList);
    }*/

    public ChatAdapter(Context context, int resource, ArrayList<Message> messageList) {
        super(context, resource, messageList);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        Message message = getItem(position);
        Log.d("MESSAGE", message.toString());
        String userConnected =Connection.getInstance().getConnection().getUser();

        String messageTo=message.getTo();

        messageTo = UserUtil.cleanUserJid(messageTo);
        userConnected = UserUtil.cleanUserJid(userConnected);

        Log.d("MESSAGE", message.toString());
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_chat_left, parent, false);
        }

        TextView messageBody = (TextView) convertView.findViewById(R.id.chat_item_friend_body);
        TextView messageFrom = (TextView) convertView.findViewById(R.id.chat_item_friend_name);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ImageLeft);


        //ENVOI DE MESSAGES
        if (!messageTo.equals(userConnected)) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_chat_right, parent, false);
            messageBody = (TextView) convertView.findViewById(R.id.chat_item_my_body);
            messageFrom = (TextView) convertView.findViewById(R.id.chat_item_my_name);
            message.setFrom((message.getFrom()==null)?Connection.getInstance().getConnection().getUser():message.getFrom());
        }

        //RECEPTION DE MESSAGES
        if(messageBody != null && messageFrom!= null){
            Bitmap bmp = ContactList.getInstance().getUser(message.getFrom()).getPhotos().get(message.getBody());
            if(bmp != null){
                Log.d("PHOTO", bmp.toString());
                imageView.setImageBitmap(bmp);
            }

            messageBody.setText(message.getBody());
            messageFrom.setText(message.getFrom());
        }


        return convertView;
    }

}
