package naoned.sil.lp.naonedchat.components.contacts;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.ArrayList;
import java.util.Collection;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.service.Connection;

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

        // Contact ListView adapter
        try {
            setListAdapter(new ContactAdapter(getActivity(), R.layout.contact_item, getContacts()));
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Contact> getContacts() throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        final ArrayList<Contact> contacts = new ArrayList<>();

        XMPPTCPConnection co = Connection.getInstance().getConnection();
        Roster roster = Roster.getInstanceFor(co);
        Collection<RosterEntry> entries = roster.getEntries();

        for (RosterEntry entry : entries) {
            // this user jaber id can be false (ex : test1@test1.fr is not correct)
            // ... so vCard will be null
            String userJaberId = entry.getUser().toString();
            VCard vCard = Connection.getInstance().getVcard(userJaberId);

            // TODO : handle bad user JaberId (ex: waiting list)... or prevent to add bad user JaberId
            // Check if not null to prevent new contact instance issue with vCard null
            if (vCard != null) {
                Contact c = new Contact(vCard.getNickName(), vCard.getJabberId(), vCard.getAvatar());
                contacts.add(c);
            }
        }

        return contacts;
    }
}
