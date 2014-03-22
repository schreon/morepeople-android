package morepeople.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.UserState;
import morepeople.android.app.morepeople.android.app.core.CoreAPI;
import morepeople.android.app.interfaces.IDataCallback;

public class EvaluationActivity extends Activity {

    private ICoreApi coreLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setTitle("morepeople");
        setContentView(R.layout.activity_evaluation);
        UserState currentState = null;
        try {
            currentState = UserState.valueOf(getIntent().getExtras().getString(Constants.PROPERTY_STATE));
        } catch (Exception e) {
            Log.e("ConfirmationActivity", e.getMessage());
        }
        coreLogic = new CoreAPI(this, currentState);

        final Button btn = (Button)findViewById(R.id.button_send_evaluation);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coreLogic.evaluate(
                    new HashMap<String, Object>(), // TODO: actually evaluate something!
                    new IDataCallback() {
                        @Override
                        public void run(Object rawData) {
                            Map<String, Object> data = (Map<String, Object>) rawData;
                            // set state
                            coreLogic.setState(UserState.valueOf((String) data.get(Constants.PROPERTY_STATE)));
                        }
                    },
                    null
                );
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.evaluation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
