package naoned.sil.lp.naonedchat.components.contacts;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import naoned.sil.lp.naonedchat.R;

/**
 * Created by julian on 23/01/16.
 */
public class ContactActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        final ListView contactListView = (ListView)findViewById(R.id.contactsList);

        ArrayList<Contact> contacts = new ArrayList<>();

        // Fake contacts
        for (int i = 0; i < 30; i++) {
            Contact c = new Contact("pseudo"+i, "contact"+i+"@gmail.com", "http://lorempixel.com/640/480/");
            contacts.add(c);
        }

        // Contact ListView adapter
        ContactAdapter contactAdapter = new ContactAdapter(this, R.layout.contact_item, contacts);
        contactListView.setAdapter(contactAdapter);
    }
}
