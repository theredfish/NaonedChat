package naoned.sil.lp.naonedchat.listeners.contact;

import android.util.Log;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

import naoned.sil.lp.naonedchat.service.Connection;

/**
 * Created by julian on 22/02/16.
 */
public class SubscriptionListener implements PacketListener {

    @Override
    public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
        Presence pres = (Presence) packet;
        if (pres.getType() != null) {
            // send back yes
            // Send the subscription request
            Presence subscription = new Presence(Presence.Type.subscribe);
            subscription.setTo(pres.getFrom());
            subscription.setPriority(24);
            subscription.setMode(Presence.Mode.available);

            try {
                Connection.getInstance().getConnection().sendPacket(subscription);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }

        Log.e("invitation", "processPacket");
    }
}
