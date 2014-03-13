package morepeople.android.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ConfirmationActivity is shown if enough users were found to start an activity
 */
public class ConfirmationActivity extends Activity {

    private BroadcastReceiver foregroundReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: extract participant list from intent and update participantsAdapter
            // write participant list into shared preferences
            SharedPreferences prefs = getBaseContext().getSharedPreferences("morepeople.android.app", Context.MODE_PRIVATE);
            String participantsListJson = (String) intent.getExtras().get("participantsListJson");
            prefs.edit().putString("participantsList", participantsListJson);
            prefs.edit().commit();

            // updates the running activity
            // deserialize participantList json
            readParticipantJson(participantsListJson);

        }
    };

    public static String BROADCAST_CONFIRMATION = "morepeople.android.app.BROADCAST_CONFIRMATION";

    private ParticipantsAdapter participantsAdapter;
    /**
     * OnCreate method
     * @param savedInstanceState contains the previous state of the activity if it was existent before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        participantsAdapter = new ParticipantsAdapter();

        ListView listView = (ListView) this.findViewById(R.id.confirm_list_view);
        listView.setAdapter(participantsAdapter);

        final LinearLayout layoutConfirmWait = (LinearLayout) this.findViewById(R.id.confirm_wait_layout);
        final LinearLayout layoutConfirmButtons = (LinearLayout) this.findViewById(R.id.confirm_button_layout);
        Button buttonConfirm = (Button) this.findViewById(R.id.button_confirm);

        /**
         * OnClickListener for confirm button.
         * Changes layout visibility.
         */
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: send confirmation to server
                layoutConfirmWait.setVisibility(View.VISIBLE);
                layoutConfirmButtons.setVisibility(View.GONE);
            }
        });

        final ConfirmationActivity self = this;

        Button buttonReject = (Button) this.findViewById(R.id.button_reject);

        /**
         * OnClickListener for reject button.
         * Opens up alert dialog where the user can cancel the search or go on with the search.
         */
        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        self);

                // set title
                alertDialogBuilder.setTitle(R.string.please_confirm_cancel);

                // set dialog message
                alertDialogBuilder
                        .setMessage(R.string.please_confirm_cancel)
                        .setCancelable(false)
                        .setPositiveButton("Absagen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // TODO: send reject to server
                                Intent intent = new Intent(self, SearchActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Ups ...", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // read from shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("morepeople.android.app", Context.MODE_PRIVATE);
        String participantsListJson = sharedPrefs.getString("participantsListJson", null);

        if(participantsListJson != null) {
            readParticipantJson(participantsListJson);
        }

        // disable static ConfirmationBackgroundReceiver
        ComponentName component=new ComponentName(this, ConfirmationBackgroundReceiver.class);
        getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        // register ConfirmationForegroundReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConfirmationActivity.BROADCAST_CONFIRMATION);
        registerReceiver(foregroundReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // enable static ConfirmationBackgroundReceiver
        ComponentName component=new ComponentName(this, ConfirmationBackgroundReceiver.class);
        getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        // unregister ConfirmationForegroundReceiver
        unregisterReceiver(foregroundReceiver);

        super.onResume();
    }

    /**
     * Returns participants adapter
     * @return participantsAdapter
     */
    public ParticipantsAdapter getParticipantsAdapter() {
        return participantsAdapter;
    }

    private void readParticipantJson(String participantsListJson) {

        /**
         *   [{
         *      'id' : '13',
         *      'name' : 'depp',
         *      'state': 'OPEN',
         *   }]
         */

        //id, name, status
        Gson gson = new Gson();
        List<Object> jsonList = gson.fromJson(participantsListJson, ArrayList.class);

        for(Object entry : jsonList ) {
            Map<String, String> entryMap = (HashMap<String, String>)entry;
            String id = entryMap.get("id");
            String name = entryMap.get("name");
            String state = entryMap.get("state");

            Participant participant = new Participant(id, name, state);
            participantsAdapter.add(participant);
        }
    }
}
