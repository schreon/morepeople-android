package morepeople.android.app;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import morepeople.android.app.interfaces.ICoreAPI;

/**
 * Created by schreon on 3/21/14.
 */
public class MatchFoundBackgroundReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("GCM", "MatchFoundBackgroundReceiver.onReceive");

        SharedPreferences prefs = context.getSharedPreferences(ICoreAPI.SHARED_PREFS, Context.MODE_PRIVATE);
        String participantsListJson = (String) intent.getExtras().get("participantsListJson");
        prefs.edit().putString("participantsList", participantsListJson);
        prefs.edit().commit();

        // create notification which will start activity if the notification is clicked
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent startConfirmationActivityIntent = new Intent(context, ConfirmationActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, startConfirmationActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] vibrate = {0, 100, 200, 300};
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Teilnehmer gefunden!")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Teilnehmer gefunden!"))
                        .setContentText("Wir haben Leute gefunden, die sich mit Dir treffen würden!")
                        .setVibrate(vibrate);
        mBuilder.setContentIntent(contentIntent);
        manager.notify(1, mBuilder.build());
        setResultCode(Activity.RESULT_OK);

        completeWakefulIntent(intent);
    }
}
