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

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Registrierung läuft ...");
        mDialog.setCancelable(false);
        mDialog.show();
        Log.d(TAG, "onCreate finished");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_welcome;
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
        Log.d(TAG, "onResume");
        mDialog.setMessage("Registrierung läuft ...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        mDialog.dismiss();
    }

    @Override
    protected  void onPoll() {
        Log.d(TAG, "onPoll");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
}
