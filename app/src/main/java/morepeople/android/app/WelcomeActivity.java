package morepeople.android.app;

import android.app.ProgressDialog;
import android.os.Bundle;

import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.ICoreFactory;

public class WelcomeActivity extends BaseActivity {
    private ProgressDialog mDialog;
    private ICoreApi coreApi;
    private ICoreFactory coreFactory;
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
