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
import java.util.ArrayList;
import java.util.Collection;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.Util.UserUtil;
import naoned.sil.lp.naonedchat.bean.User;
import naoned.sil.lp.naonedchat.service.Connection;

/**
 * Created by julian on 23/01/16.
 */
public class ContactListFragment extends ListFragment {

    private ViewGroup rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.contact_list, container, false
        );

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Contact ListView adapter
        try {
            setListAdapter(new ContactAdapter(getActivity(), R.layout.contact_item, getUsers()));
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getUsers() throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        final ArrayList<User> users = new ArrayList<>();

        XMPPTCPConnection co = Connection.getInstance().getConnection();
        Roster roster = Roster.getInstanceFor(co);
        Collection<RosterEntry> entries = roster.getEntries();

        for (RosterEntry entry : entries) {
            // this user jaber id can be false (ex : test1@test1.fr is not correct)...

            String userJaberId = entry.getUser();
            Connection conn = Connection.getInstance();

            // ... so we need to be sure that user exists
            if (UserUtil.userExists(entry.getName())) {
                User user =  conn.getUser(userJaberId);

                users.add(new User(user.getVCard()));
            }
        }

        return users;
    }
}
