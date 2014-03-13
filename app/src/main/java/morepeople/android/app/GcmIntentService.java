package morepeople.android.app;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * GcmIntentService extends IntentService and provides onHandleIntent method
 */
public class GcmIntentService extends IntentService {

    /**
     * Constructor of GcmIntentService class
     */
    public GcmIntentService() {
        super("GcmIntentService");
    }

    /**
     * OnHandleIntent
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // dependent on the gcm message type etc. broadcast another intent

        // if the status code indicates, that no activity has intercepted the intent:
        // create notification

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            // kick off broadcast stuff
            if (extras.get("message_type").equals("CONFIRMATION")) {
                intent.setAction(ConfirmationActivity.BROADCAST_CONFIRMATION);
                sendBroadcast(intent);
            }
        }

        // TODO: the same with chat messages
    }
}
