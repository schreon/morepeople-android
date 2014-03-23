package morepeople.android.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * MainBroadcastReceiver extends WakefulBroadcastReceiver and provides the onReceive method
 */
public class MainBroadcastReceiver extends WakefulBroadcastReceiver {
    private final static String TAG = "morepeople.android.app.MainBroadcastReceiver";

    /**
     * OnReceive method sets wakeful service
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        ComponentName comp = new ComponentName(context.getPackageName(), MainIntentService.class.getName());
        Intent startIntent = intent.setComponent(comp);
        startWakefulService(context, startIntent);
        setResultCode(Activity.RESULT_OK);
    }
}
