package morepeople.android.app;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by schreon on 3/13/14.
 */
public class ConfirmationBackgroundService extends IntentService {
    public ConfirmationBackgroundService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("GCM", "ConfirmationBackgroundService.onHandleIntent");
        // write participant list into shared preferences
        SharedPreferences prefs = this.getSharedPreferences("morepeople.android.app", Context.MODE_PRIVATE);
        String participantsListJson = (String) intent.getExtras().get("participantsListJson");
        prefs.edit().putString("participantsList", participantsListJson);
        prefs.edit().commit();

        // TODO: create notification and vibrate heavily
        // TODO: if the user clicks the notification, start activity

        // create notification which will start activity if the notification is clicked
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent startConfirmationActivityIntent = new Intent(this, ConfirmationActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, startConfirmationActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] vibrate = {0, 100, 200, 300 };
        NotificationCompat.Builder nBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Teilnahme bestätigt!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Bumswurschtegal"))
                .setContentText("Bumswurschtegal")
                .setVibrate(vibrate);

        // Complete wakeful intent
        ConfirmationBackgroundReceiver.completeWakefulIntent(intent);
    }
}
