package naoned.sil.lp.naonedchat.listeners.authentification;

import android.support.design.widget.Snackbar;
import android.view.View;

import naoned.sil.lp.naonedchat.service.Connection;

/**
 * Created by ACHP on 22/01/2016.
 */
public class DisconnectOnClickListener implements View.OnClickListener {
    public void onClick(View v) {
        Connection.getInstance().disconnect();
        Snackbar.make(v, "Déconexion en cours ...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
