package morepeople.android.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

public class WelcomeActivity extends BaseActivity {
    private ProgressDialog mDialog;
    public static final String TAG = "morepeople.android.app.WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getActionBar().setTitle("morepeople");

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Registrierung läuft ...");
        mDialog.setCancelable(false);
        mDialog.show();
        Log.d(TAG, "onCreate finished");
    }

    @Override
    protected void onCoreInitFinished() {
        super.onCoreInitFinished();
        Log.d(TAG, "starting onCoreInitFinished");
        coreApi.loadState(defaultErrorCallback);
        Log.d(TAG, "finishing onCoreInitFinished");
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
