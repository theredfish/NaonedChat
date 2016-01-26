package naoned.sil.lp.naonedchat.contact;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import naoned.sil.lp.naonedchat.R;

/**
 * Created by julian on 22/01/16.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
    Context context;
    int layoutResourceId;
    ArrayList<Contact> data = new ArrayList<>();

    public ContactAdapter(Context context, int layoutResourceId,ArrayList<Contact>data){
        super(context,layoutResourceId,data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ContactHolder contactHolder;

        Log.e("ContactAdapter", "is row null? " + (row == null));
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

        Contact contact = data.get(position);
        contactHolder.pseudo.setText(contact.getPseudo());

        Picasso.with(context)
                .load(contact.getPicture())
                .resize(100,100)
                .centerCrop()
                .into(contactHolder.picture);

        return row;
    }

    // Contact holder with picture and pseudo
    static class ContactHolder {
        ImageView picture;
        TextView pseudo;
    }
}