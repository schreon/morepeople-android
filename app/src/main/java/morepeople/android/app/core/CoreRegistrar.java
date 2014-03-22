package morepeople.android.app.core;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.ICoreRegistrar;
import morepeople.android.app.interfaces.IDataCallback;
import morepeople.android.app.interfaces.IErrorCallback;

/**
 * Created by schreon on 3/20/14.
 */
public class CoreRegistrar implements ICoreRegistrar {
    private static final String SENDER_ID = "1039190776751";
    private final static String TAG = "morepeople.android.app.core.CoreRegistrar";
    private Context context;
    private GoogleCloudMessaging gcm;

    public CoreRegistrar(Context context) {
        this.context = context;
    }

    @Override
    public void register(final IDataCallback onSuccess, final IErrorCallback onError) {
        final Map<String, Object> resultMap = new HashMap<String, Object>();
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String msg;
                try {
                    if (gcm == null) {
                        Context appContext = context.getApplicationContext();
                        gcm = GoogleCloudMessaging.getInstance(appContext);
                    }

                    String userId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + userId;
                    resultMap.put(Constants.PROPERTY_USER_ID, userId);
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
                Log.d(TAG, "onPostExecute");
                onSuccess.run(resultMap);
            }

        }).execute();
    }
}
