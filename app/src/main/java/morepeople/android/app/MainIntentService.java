package morepeople.android.app;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * MainIntentService extends IntentService and provides onHandleIntent method
 */
public class MainIntentService extends IntentService {

    /**
     * Constructor of MainIntentService class
     */
    public MainIntentService() {
        super("MainIntentService");
    }

    /**
     * OnHandleIntent
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("GCM", "got an intent");
        // dependent on the gcm message type etc. broadcast another intent

        // if the status code indicates, that no activity has intercepted the intent:
        // create notification

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            // kick off broadcast stuff
            if (extras.get("MP_MESSAGE_TYPE").equals("CONFIRMATION")) {
                Log.d("GCM", "CONFIRMATION");
                Intent mIntent = new Intent(ConfirmationActivity.BROADCAST_CONFIRMATION);

                mIntent.putExtra("participantsListJson", "[{'id':'test', 'name':'test', 'status':'OPEN'}]");

                sendBroadcast(mIntent);
            }
        }

        MainBroadcastReceiver.completeWakefulIntent(intent);
        // TODO: the same with chat messages
    }
}
