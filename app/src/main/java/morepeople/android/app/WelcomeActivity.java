package morepeople.android.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import morepeople.android.app.morepeople.android.app.core.CoreAPI;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.IDataCallback;

public class WelcomeActivity extends Activity {
    ProgressDialog mDialog;
    ICoreApi coreLogic;
    public static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getActionBar().setTitle("morepeople");

        mDialog = new ProgressDialog(this);
        // Start CoreApi with no start state
        coreLogic = new CoreAPI(this, null);
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
        coreLogic.initialize(onError);
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
