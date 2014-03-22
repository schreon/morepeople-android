package morepeople.android.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
