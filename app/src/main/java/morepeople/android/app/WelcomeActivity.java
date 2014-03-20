package morepeople.android.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import morepeople.android.app.morepeople.android.app.core.CoreLogic;
import morepeople.android.app.morepeople.android.app.core.ICoreLogic;
import morepeople.android.app.morepeople.android.app.core.IDataCallback;

public class WelcomeActivity extends Activity {
    ProgressDialog mDialog;
    ICoreLogic coreLogic;
    public static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mDialog = new ProgressDialog(this);
        // Start CoreLogic with no start state
        coreLogic = new CoreLogic(this, null);
        final Context context = this;
        IDataCallback onError = new IDataCallback() {
            @Override
            public void run(final Object data) {
                Log.d(TAG, "loadStateError");
                Handler mainHandler = new Handler(context.getMainLooper());

                Runnable runOnUI = new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, data.toString(), Toast.LENGTH_LONG).show();
                    }
                }; // This is your code
                mainHandler.post(runOnUI);
            }
        };

        // Load state from server
        coreLogic.load(onError);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mDialog.setMessage("Registrierung läuft ...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDialog.dismiss();
    }
}
