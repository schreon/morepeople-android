package morepeople.android.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import morepeople.android.app.core.CoreWritablePreferences;
import morepeople.android.app.interfaces.ICoreWritablePreferences;
import morepeople.android.app.structures.UserState;

/**
 * Created by schreon on 3/23/14.
 */
public class MatchRunningBackgroundReceiver extends WakefulBroadcastReceiver {
    private final static String TAG = "morepeople.android.app.MatchRunningBackgroundReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        // get preferences
        ICoreWritablePreferences preferences = new CoreWritablePreferences(context);
        preferences.setCurrentUserState(UserState.RUNNING);

        // create notification which will start the activity if the notification is clicked
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent startChatActivity = new Intent(context, ChatActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, startChatActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] vibrate = {0, 100, 200, 300};
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Es geht los!")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Es geht los!"))
                        .setContentText("Alle haben zugesagt - bitte komme jetzt zum Treffpunkt!")
                        .setVibrate(vibrate);
        mBuilder.setContentIntent(contentIntent);
        manager.notify(1, mBuilder.build());

        Intent startIntent = new Intent(context, ChatActivity.class);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startIntent);
        completeWakefulIntent(intent);
    }
}
