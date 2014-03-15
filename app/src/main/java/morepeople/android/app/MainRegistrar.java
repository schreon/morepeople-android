package morepeople.android.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by schreon on 3/13/14.
 */
public class MainRegistrar {
    public static final String PROPERTY_REG_ID = "REG_ID";
    private static final String PROPERTY_APP_VERSION = "APP_VERSION";

    public static final String SENDER_ID = "1039190776751";

    private final static String TAG = "GCM";

    private static GoogleCloudMessaging gcm;
    private static String regId;

    private MainRegistrar() {}

    /**
     * Requests a GCM registration id and puts it into the shared preferences
     * @param context
     * @param callback will be called as soon as the reg id is available
     */
    public static void requestRegistrationId(Context context, Runnable callback) {
        final SharedPreferences prefs = context.getSharedPreferences("MorePeople", Context.MODE_PRIVATE);
        String regId = prefs.getString(PROPERTY_REG_ID, "");
        if (regId.isEmpty()) {
            for (String key : prefs.getAll().keySet()) {
                Log.d(TAG, "-> " + key + " = " + prefs.getAll().get(key));
            }

            Log.d(TAG, "Requesting new regid");
            registerInBackground(context, callback);
        } else {
            Log.d(TAG, "Found reg id: "+ regId);
            callback.run();
        }
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private static void registerInBackground(Context context, Runnable callback) {
        final Context finContext = context;
        final Runnable fCallback = callback;
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Looper.prepare();
                String msg;
                try {
                    if (gcm == null) {
                        Context appContext = finContext.getApplicationContext();
                        gcm = GoogleCloudMessaging.getInstance(appContext);
                    }

                    regId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regId;
                    storeRegistrationId(finContext.getApplicationContext(), regId);

                } catch (IOException ex) {
                    msg = "Error:" + ex.getMessage();
                }
                Log.d(TAG, msg);
                Looper.loop();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d("MainRegistrar", "onPostExecute");
                fCallback.run();
            }
        }).execute();
    }

    private static void storeRegistrationId(Context applicationContext, String regId) {
        Log.d(TAG, "storing reg id");
        final SharedPreferences prefs = applicationContext.getSharedPreferences("MorePeople", Context.MODE_PRIVATE);;
        int appVersion = getAppVersion(applicationContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
        Log.d(TAG, "successfully stored regid");
    }
}
