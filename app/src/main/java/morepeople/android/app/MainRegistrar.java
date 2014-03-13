package morepeople.android.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
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

    public static String requestRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String regId = prefs.getString(PROPERTY_REG_ID, "");
        if (regId.isEmpty()) {
            Log.d(TAG, "Requesting new regid");
            registerInBackground(context);
            return "";
        }
        Log.d(TAG, "found reg id: "+ regId);
        return regId;
    }

    private static SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences("GCM_PREFERENCES", Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private static void registerInBackground(Context context) {
        final Context finContext = context;
        AsyncTask backgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                String msg;
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(finContext.getApplicationContext());
                    }

                    regId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regId;
                    storeRegistrationId(finContext.getApplicationContext(), regId);

                } catch (IOException ex) {
                    msg = "Error:" + ex.getMessage();
                }
                Log.d(TAG, msg);
                return msg;
            }
        };

        backgroundTask.execute(null, null, null);
    }

    private static void storeRegistrationId(Context applicationContext, String regId) {
        Log.d(TAG, "storing reg id");
        final SharedPreferences prefs = getGCMPreferences(applicationContext);
        int appVersion = getAppVersion(applicationContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
        Log.d(TAG, "successfully stored regid");
    }
}
