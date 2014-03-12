package morepeople.android.app;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by schreon on 3/12/14.
 */
public class GcmIntentService extends IntentService {
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
