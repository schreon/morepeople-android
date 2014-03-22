package morepeople.android.app;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.structures.UserState;

public class CancelActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        getActionBar().setTitle("morepeople");
    }

    @Override
    protected void onCoreInitFinished() {
        final Button buttonConfirmCancel = (Button) this.findViewById(R.id.button_confirmcancel);
        buttonConfirmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coreApi.confirmCancel(defaultErrorCallback);
            }
        });

    }
}
