package naoned.sil.lp.naonedchat.components.contacts;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import java.util.ArrayList;
import java.util.Collection;

import naoned.sil.lp.naonedchat.R;
import naoned.sil.lp.naonedchat.Util.UserUtil;
import naoned.sil.lp.naonedchat.bean.Chat;
import naoned.sil.lp.naonedchat.bean.ContactList;
import naoned.sil.lp.naonedchat.bean.User;
import naoned.sil.lp.naonedchat.components.lastContacts.ScreenSlideActivity;
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("TEST", Integer.toString(position));
        User u = ContactList.getInstance().getUser(position);
        Chat.getInstance().init(u);
        ScreenSlideActivity.getInstance().refreshAdapter();
        ViewPager mPager=(ViewPager) getActivity().findViewById(R.id.pager);
        mPager.setCurrentItem(1);


    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Button addUserButton = (Button) rootView.findViewById(R.id.addUserButton);
        final EditText addUserText = (EditText) rootView.findViewById(R.id.addUserText);

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

        addUserButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userEditText = addUserText.getText().toString();

                if (userEditText.isEmpty()) {
                    Toast.makeText(rootView.getContext(), "Veuillez saisir un nom d'utilisateur", Toast.LENGTH_SHORT).show();
                } else {
                    if (UserUtil.userExists(userEditText)) {
                        if (UserUtil.addUser(userEditText, Connection.getInstance().getConnection().getUser())) {
                            Toast.makeText(rootView.getContext(), "Utilisateur ajouté", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(rootView.getContext(), "Impossible d'ajouter l'utilisateur.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(rootView.getContext(), "Cet utilisateur n'existe pas.", Toast.LENGTH_SHORT).show();
                    }
                }

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
        });
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
            // And we need to fix their very bad lib!! :[ inconsitant JID...
            String validUserJid = (entry.getName() == null ? entry.getUser() : entry.getName());

            if (UserUtil.userExists(validUserJid)) {
                User user =  conn.getUser(userJaberId);

                users.add(new User(user.getVCard()));
            }
        }

        return users;
    }
}
