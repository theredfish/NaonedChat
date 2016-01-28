package naoned.sil.lp.naonedchat.Util;

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
}
