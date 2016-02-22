package naoned.sil.lp.naonedchat.Util;

import android.util.Log;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;

import naoned.sil.lp.naonedchat.bean.User;
import naoned.sil.lp.naonedchat.components.lastContacts.ScreenSlideActivity;
import naoned.sil.lp.naonedchat.service.Connection;

/**
 * Created by ACHP on 28/01/2016.
 */
public class UserUtil {
    public static String cleanUserJid(String Jid) {
        if (!Jid.contains("/")) {
            return Jid;
        }
        return Jid.split("/")[0];
    }

    /**
     * Check if a specific user jid exists
     * See solution here : http://stackoverflow.com/a/14214569
     *
     * Example of valid JID parameter : jeanLuck.
     * Example of invalid JID parameter : jeanLuck@naonedchat.fr
     *
     * @param jid the specified user jabber ID (important : without service domain).
     * @return true if user exists, else return false
     */
    public static boolean userExists(String jid) {
        String serviceName = Connection.getInstance().getConnection().getServiceName();
        UserSearchManager search = new UserSearchManager(Connection.getInstance().getConnection());

        if (jid.contains("@")) {
            jid = jid.split("@")[0];
        }

        Form searchForm = null;

        // Initialize search service activated on our OpenFire server
        try {
            searchForm = search.getSearchForm("search." + serviceName);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }

        Form answerForm = searchForm.createAnswerForm();
        answerForm.setAnswer("Username", true);

        // exact user JID without service domain
        answerForm.setAnswer("search", jid);
        ReportedData data = null;

        // Get search result
        try {
            data = search.getSearchResults(answerForm, "search." + serviceName);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

        // Check if the result contains the user with the specified jid
        if (data.getRows() != null) {
            for (ReportedData.Row row: data.getRows()) {
                for (String value: row.getValues("jid")) {
                    // if we are in this part then we are in it.hasNext()... so there is a result
                    // we can conclude that user exists
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean addUser(String jid, String currentJid) {
        Log.e("jids", jid + " - current = " + currentJid);
        XMPPConnection connection = Connection.getInstance().getConnection();
        Roster roster = Roster.getInstanceFor(connection);

        currentJid = UserUtil.cleanUserJid(currentJid);
        String jidWithService = jid + "@" + connection.getServiceName();

        if (currentJid.equals(jidWithService)) {
            Log.e("JId", "equals");
            return false;
        }

        if (userExists(jid)) {
            Log.e("Demande : ", jid + "@" + connection.getServiceName());
            RosterEntry rosterEntry = roster.getEntry(jid + "@" + connection.getServiceName());
            Log.e("rosterENtry null?", "" + (roster == null));

            // Send the subscription request
            Presence subscription = new Presence(Presence.Type.subscribe);
            subscription.setTo(jidWithService);

            try {
                connection.sendStanza(subscription);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }

            ScreenSlideActivity.getInstance().refreshAdapter();

            return true;
        }

        return false;
    }
}
