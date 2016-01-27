package naoned.sil.lp.naonedchat.components.contacts;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import naoned.sil.lp.naonedchat.R;

/**
 * Created by julian on 23/01/16.
 */
public class ContactListFragment extends ListFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.contact_list, container, false
        );

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ArrayList<Contact> contacts = new ArrayList<>();

        // Fake contacts
        for (int i = 0; i < 30; i++) {
            Contact c = new Contact("pseudo"+i, "contact"+i+"@gmail.com", "http://lorempixel.com/640/480/");
            contacts.add(c);
        }

        // Contact ListView adapter
        setListAdapter(new ContactAdapter(getActivity(), R.layout.contact_item, contacts));
    }
}
