package morepeople.android.app;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

    protected void beforeCoreApi() {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        beforeCoreApi();

        context = this;
        coreApi = null;

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

    protected void onCoreInitFinished() {
        Log.d(TAG, "onCoreInitFinished");
        coreApi.loadState(defaultErrorCallback);

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
        pollHandler.removeCallbacks(poller);
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        pollHandler.removeCallbacks(poller);
        Log.d(TAG, "onPause");
    }
}
