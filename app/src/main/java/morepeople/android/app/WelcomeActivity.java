package morepeople.android.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Map;

public class WelcomeActivity extends Activity {
    ProgressDialog mDialog;
    CoreLogic coreLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mDialog = new ProgressDialog(this);
        coreLogic = new CoreLogic(this);
        final Context context = this;
        IDataCallback onSuccess = new IDataCallback() {
            @Override
            public void run(Map<String, Object> data) {
                // TODO: start the search activity

            }
        };

        IDataCallback onError = new IDataCallback() {
            @Override
            public void run(Map<String, Object> data) {
                // TODO: show toast and retry
            }
        };
        coreLogic.load(onSuccess, onError);
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
