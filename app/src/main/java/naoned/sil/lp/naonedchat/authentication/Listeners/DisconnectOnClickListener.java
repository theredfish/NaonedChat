package naoned.sil.lp.naonedchat.authentication.Listeners;

import android.support.design.widget.Snackbar;
import android.view.View;

import service.XmppService;

/**
 * Created by ACHP on 22/01/2016.
 */
public class DisconnectOnClickListener implements View.OnClickListener {

    private XmppService xmppService;


    public DisconnectOnClickListener(XmppService xmppService){
        this.xmppService = xmppService;
    }

    @Override
    public void onClick(View v) {
        xmppService.disconnect();

        Snackbar.make(v, "Déconexion en cours ...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
