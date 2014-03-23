package morepeople.android.app;

import android.app.Activity;
import android.app.NotificationManager;
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

    private Runnable poller = null;
    protected long pollDelay;
    private Handler pollHandler;

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
        setContentView(getLayoutResourceId());
        context = this;
        coreApi = null;
        pollHandler = new Handler(getMainLooper());
        Log.d(TAG, "onCreate finished");
        pollDelay = 10000;
        poller = new Runnable() {
            @Override
            public void run() {
                pollHandler.removeCallbacks(poller);
                Log.d(TAG, "poller");
                onPoll();
                pollHandler.postDelayed(poller, pollDelay);
            }
        };
    }

    protected abstract int getLayoutResourceId();

    protected synchronized void onPoll() {
        // nothing
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

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        pollHandler.removeCallbacks(poller);
        pollHandler.postDelayed(poller, pollDelay);
        // Clear notifications if necessary
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        pollHandler.removeCallbacks(poller);
        Log.d(TAG, "onPause");
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        pollHandler.removeCallbacks(poller);
        poller.run();
        Log.d(TAG, "onUserInteraction");
    }
}
