package morepeople.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import morepeople.android.app.morepeople.android.app.core.CoreLogic;
import morepeople.android.app.morepeople.android.app.core.ICoreLogic;
import morepeople.android.app.morepeople.android.app.core.IDataCallback;

public class CancelActivity extends Activity {

    private ICoreLogic coreLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);

        ICoreLogic.UserState currentState = null;
        try {
            currentState = ICoreLogic.UserState.valueOf(getIntent().getExtras().getString(ICoreLogic.PROPERTY_STATE));
        } catch (Exception e) {
            Log.e("ConfirmationActivity", e.getMessage());
        }
        coreLogic = new CoreLogic(this, currentState);

        final Button buttonConfirmCancel = (Button) this.findViewById(R.id.button_confirmcancel);
        buttonConfirmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coreLogic.confirmCancel(new IDataCallback() {
                    @Override
                    public void run(Object data) {
                        coreLogic.load(null);
                    }
                }, null);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cancel, menu);
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
