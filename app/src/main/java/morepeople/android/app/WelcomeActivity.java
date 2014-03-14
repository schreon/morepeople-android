package morepeople.android.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }


    @Override
    protected void onResume() {
        super.onResume();
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Registrierung läuft ...");
        mDialog.setCancelable(false);
        mDialog.show();
        MainRegistrar.requestRegistrationId(this, new Runnable() {
            @Override
            public void run() {
                Log.d("GCM", "got reg id, dismissing loading screen");
                mDialog.dismiss();
                // TODO: broadcast, that loading is finished
            }
        });
    }
}
