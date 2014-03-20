package morepeople.android.app.morepeople.android.app.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by schreon on 3/20/14.
 */
public class CoreRegistrar implements ICoreRegistrar {
    private final static String TAG = "CoreRegistrar";
    private Context context;
    private GoogleCloudMessaging gcm;
    private String regId;

    public CoreRegistrar(Context context) {
        this.context = context;
    }

    @Override
    public String getRegistrationId() {
        final SharedPreferences prefs = context.getSharedPreferences(ICoreLogic.SHARED_PREFS, Context.MODE_PRIVATE);
        String regId = prefs.getString(PROPERTY_REG_ID, "");
        if (regId.isEmpty()) {
            for (String key : prefs.getAll().keySet()) {
                Log.d(TAG, "-> " + key + " = " + prefs.getAll().get(key));
            }
            return null;
        } else {
            return regId;
        }
    }

    @Override
    public void requestRegistrationId(IDataCallback onSuccess, IDataCallback onError) {
        final SharedPreferences prefs = context.getSharedPreferences(ICoreLogic.SHARED_PREFS, Context.MODE_PRIVATE);
        String regId = prefs.getString(PROPERTY_REG_ID, "");
        if (regId.isEmpty()) {
            for (String key : prefs.getAll().keySet()) {
                Log.d(TAG, "-> " + key + " = " + prefs.getAll().get(key));
            }

            Log.d(TAG, "Requesting new regid");
            registerInBackground(context, onSuccess, onError);
        } else {
            Log.d(TAG, "Found reg id: " + regId);
            onSuccess.run(regId);
        }
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground(final Context context, final IDataCallback onSuccess, final IDataCallback onError) {
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String msg;
                try {
                    if (gcm == null) {
                        Context appContext = context.getApplicationContext();
                        gcm = GoogleCloudMessaging.getInstance(appContext);
                    }

                    regId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regId;
                    storeRegistrationId(context.getApplicationContext(), regId);

                } catch (IOException ex) {
                    msg = "Error:" + ex.getMessage();
                    onError.run(msg);
                }
                Log.d(TAG, msg);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d("MainRegistrar", "onPostExecute");
                onSuccess.run(regId);
            }
        }).execute();
    }

    private void storeRegistrationId(Context applicationContext, String regId) {
        Log.d(TAG, "storing reg id");
        final SharedPreferences prefs = applicationContext.getSharedPreferences(ICoreLogic.SHARED_PREFS, Context.MODE_PRIVATE);
        ;
        int appVersion = getAppVersion(applicationContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
        Log.d(TAG, "successfully stored regid");
    }
}
