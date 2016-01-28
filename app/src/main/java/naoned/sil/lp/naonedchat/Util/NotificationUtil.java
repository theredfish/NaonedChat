package naoned.sil.lp.naonedchat.Util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import naoned.sil.lp.naonedchat.R;

/**
 * Created by ACHP on 28/01/2016.
 */
public class NotificationUtil {



    public static void makeNotification(Context context){
        Log.d("zfzef", "zefzefzef");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext())
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app


        NotificationManager mNotificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(659, mBuilder.build());
    }
}
