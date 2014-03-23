package morepeople.android.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;

import morepeople.android.app.interfaces.Constants;

public class WelcomeActivity extends BaseActivity {
    private ProgressDialog mDialog;
    public static final String TAG = "morepeople.android.app.WelcomeActivity";

    @Override
    protected void beforeCoreApi() {
        boolean isDebuggable =  ( 0 != ( getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (isDebuggable) {
            wipeSharedPrefs();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Registrierung läuft ...");
        mDialog.setCancelable(false);
        mDialog.show();
        Log.d(TAG, "onCreate finished");
    }

    private void wipeSharedPrefs() {
        Log.d(TAG, "wipe shared prefs");
        getSharedPreferences(Constants.PROPERTY_SHARED_PREFS, Context.MODE_PRIVATE).edit().clear().commit();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onCoreInitFinished() {
        super.onCoreInitFinished();
        Log.d(TAG, "onCoreInitFinished");
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
    protected void onPoll() {
        Log.d(TAG, "onPoll");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
}
