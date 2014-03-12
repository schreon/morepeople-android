package morepeople.android.app;

import android.app.IntentService;
import android.content.Intent;

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

    }
}
