package naoned.sil.lp.naonedchat.components.contacts;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.Util.DrawableUtil;
import naoned.sil.lp.naonedchat.Util.ImageViewUtil;
import naoned.sil.lp.naonedchat.bean.User;

/**
 * Created by julian on 22/01/16.
 */
public class ContactAdapter extends ArrayAdapter<User> {
    Context context;
    int layoutResourceId;
    ArrayList<User> data = new ArrayList<>();

    public ContactAdapter(Context context, int layoutResourceId, ArrayList<User> data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ContactHolder contactHolder;

        // If null, we create new one
        if (row == null) {
            // Inflate to get contact_item layout
            LayoutInflater inflater=((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            contactHolder = new ContactHolder();
            contactHolder.picture = (ImageView)row.findViewById(R.id.contactPicture);
            contactHolder.pseudo = (TextView)row.findViewById(R.id.contactPseudo);

            row.setTag(contactHolder);
        } else {
            contactHolder = (ContactHolder)row.getTag();
        }

        User user = data.get(position);

        // set avatar from vCard if exists, else we set default avatar.
        if (user.getVCard().getAvatar() != null) {
            user.setAvatar(user.getVCard().getAvatar());
        } else {
            byte[] avatar = DrawableUtil.getDrawableByte(ContextCompat.getDrawable(this.context, R.drawable.default_avatar));
            user.setAvatar(avatar);
        }

        contactHolder.pseudo.setText(user.getNickname());
        contactHolder.picture.setImageBitmap(user.getAvatar());

        // resize and center crop picture
        ImageViewUtil.resize(contactHolder.picture, 100, 100);
        ImageViewUtil.centerCrop(contactHolder.picture);

        return row;
    }

    // Contact holder with picture and pseudo
    static class ContactHolder {
        ImageView picture;
        TextView pseudo;
    }
}