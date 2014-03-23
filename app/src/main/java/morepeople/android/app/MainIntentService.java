package morepeople.android.app;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import morepeople.android.app.interfaces.Constants;

/**
 * MainIntentService extends IntentService and provides onHandleIntent method
 */
public class MainIntentService extends IntentService {
    private final static String TAG = "morepeople.android.app.MainIntentService";
    /**
     * Constructor of MainIntentService class
     */
    public MainIntentService() {
        super("MainIntentService");
    }

    private void sendLocalBroadcast(String localBroadCast) {
        Log.d(TAG, "sendLocalBroadcast -> " + localBroadCast);
        Intent mIntent = new Intent();
        mIntent.setAction(localBroadCast);
        sendBroadcast(mIntent);
    }
    /**
     * OnHandleIntent
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        // dependent on the gcm message type etc. broadcast another intent

        // if the STATE code indicates, that no activity has intercepted the intent:
        // create notification

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        Log.d(TAG, "messageType -> " + messageType);
        if (extras != null) {
            // kick off broadcast stuff
            String mpMessageType = (String)extras.get(Constants.PROPERTY_MESSAGE_TYPE);
            Log.d(TAG, "mpMessageType -> " + mpMessageType);
            if (mpMessageType.equals(Constants.BROADCAST_GCM_CONFIRMATION)) {
                sendLocalBroadcast(Constants.BROADCAST_LOCAL_CONFIRMATION);
            }
            if (mpMessageType.equals(Constants.BROADCAST_GCM_MATCH_FOUND)) {
                sendLocalBroadcast(Constants.BROADCAST_LOCAL_MATCH_FOUND);
            }
            if (mpMessageType.equals(Constants.BROADCAST_GCM_CHAT)) {
                sendLocalBroadcast(Constants.BROADCAST_LOCAL_CHAT);
            }
        }

        MainBroadcastReceiver.completeWakefulIntent(intent);
        Log.d(TAG, "onHandleIntent complete");
    }
}
