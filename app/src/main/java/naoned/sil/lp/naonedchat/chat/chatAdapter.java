package naoned.sil.lp.naonedchat.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jivesoftware.smack.packet.Message;

import naoned.sil.lp.naonedchat.R;
import service.Connection;

/**
 * Created by ACHP on 23/01/2016.
 */
public class chatAdapter extends ArrayAdapter<Message> {
    public chatAdapter(Context context, int resource, Message[] messageList) {
        super(context, resource, messageList);

    }


    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);
        String userConnected =Connection.getInstance().getConnection().getUser();
        String messageTo=message.getTo();


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_chat_left, parent, false);
        }
        TextView messageBody = (TextView) convertView.findViewById(R.id.chat_item_friend_body);
        TextView messageFrom = (TextView) convertView.findViewById(R.id.chat_item_friend_name);

        if (!messageTo.equals(userConnected)) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_chat_right, parent, false);
                messageBody = (TextView) convertView.findViewById(R.id.chat_item_my_body);
                messageFrom = (TextView) convertView.findViewById(R.id.chat_item_my_name);
            message.setFrom((message.getFrom()==null)?Connection.getInstance().getConnection().getUser():message.getFrom());
        }




        messageBody.setText(message.getBody());
        messageFrom.setText(message.getFrom());
        return convertView;
    }

}
