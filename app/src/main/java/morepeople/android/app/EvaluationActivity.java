package morepeople.android.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class EvaluationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        getActionBar().setTitle("morepeople");
    }

    @Override
    protected void onCoreInitFinished() {

        final Button btn = (Button) findViewById(R.id.button_send_evaluation);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coreApi.evaluate(
                        new HashMap<String, Object>(), // TODO: actually evaluate something!
                        defaultErrorCallback
                );
            }
        });
    }

}
