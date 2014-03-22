package morepeople.android.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import morepeople.android.app.factory.CoreFactory;
import morepeople.android.app.interfaces.IApiCallback;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.ICoreFactory;
import morepeople.android.app.interfaces.IErrorCallback;

/**
 * Created by schreon on 3/22/14.
 */
public abstract class BaseActivity extends Activity {
    protected ICoreApi coreApi;
    private static final String TAG = "morepeople.android.app.BaseActivity";
    private Context context;

    protected IErrorCallback defaultErrorCallback = new IErrorCallback() {
        @Override
        public void run(final String errorMessage) {
            Log.d(TAG, errorMessage);
            Handler mainHandler = new Handler(getMainLooper());

            Runnable runOnUI = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                }
            };
            mainHandler.post(runOnUI);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        coreApi = null;

        Log.d(TAG, "onCreate finished");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(TAG, "starting onPostCreate");
        if (coreApi == null) {
            ICoreFactory coreFactory = new CoreFactory(this);
            coreFactory.createCoreApi(
                    null,
                    new IApiCallback() {
                        @Override
                        public void run(ICoreApi pCoreApi) {
                            coreApi = pCoreApi;
                            onCoreInitFinished();
                        }
                    },
                    defaultErrorCallback
            );
        }
        Log.d(TAG, "finishing onPostCreate");
    }

    protected void onCoreInitFinished() {
        Log.d(TAG, "starting onCoreInitFinished");
        Log.d(TAG, "finishing onCoreInitFinished");
    }
}
