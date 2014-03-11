package morepeople.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class ConfirmationActivity extends Activity {

    private ParticipantsAdapter participantsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        participantsAdapter = new ParticipantsAdapter();
        ListView listView = (ListView) this.findViewById(R.id.confirm_list_view);
        listView.setAdapter(participantsAdapter);


    }

    public ParticipantsAdapter getParticipantsAdapter() {
        return participantsAdapter;
    }
}
