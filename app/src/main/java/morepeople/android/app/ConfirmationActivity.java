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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.structures.Participant;
import morepeople.android.app.structures.UserState;
import morepeople.android.app.morepeople.android.app.core.CoreAPI;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.IDataCallback;

/**
 * ConfirmationActivity is shown if enough users were found to start a match.
 */
public class ConfirmationActivity extends Activity {

    public static String BROADCAST_CONFIRMATION = "morepeople.android.app.BROADCAST_CONFIRMATION";
    private ICoreApi coreLogic;
    private BroadcastReceiver foregroundReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("GCM", "ConfirmationActivity.foregroundReceiver");
            // write participant list into shared preferences
            SharedPreferences prefs = getBaseContext().getSharedPreferences("morepeople.android.app", Context.MODE_PRIVATE);
            String participantsListJson = intent.getStringExtra("participantsListJson");
            prefs.edit().putString("participantsList", participantsListJson);
            prefs.edit().commit();

            // updates the running activity
            // deserialize participantList json
            readParticipantJson(participantsListJson);
        }
    };
    private ParticipantsAdapter participantsAdapter;

    /**
     * OnCreate method
     *
     * @param savedInstanceState contains the previous state of the activity if it was existent before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        getActionBar().setTitle("Es geht los!");

        UserState currentState = null;
        try {
            currentState = UserState.valueOf(getIntent().getExtras().getString(Constants.PROPERTY_STATE));
        } catch (Exception e) {
            Log.e("ConfirmationActivity", e.getMessage());
        }
        coreLogic = new CoreAPI(this, currentState);

        participantsAdapter = new ParticipantsAdapter(coreLogic);

        ListView listView = (ListView) this.findViewById(R.id.confirm_list_view);
        listView.setAdapter(participantsAdapter);

        final LinearLayout layoutConfirmWait = (LinearLayout) this.findViewById(R.id.confirm_wait_layout);
        final LinearLayout layoutConfirmButtons = (LinearLayout) this.findViewById(R.id.confirm_button_layout);
        Button buttonConfirm = (Button) this.findViewById(R.id.button_confirm);

        // Hide controls if already in accepted state
        if (currentState.equals(UserState.ACCEPTED)) {
            layoutConfirmWait.setVisibility(View.VISIBLE);
            layoutConfirmButtons.setVisibility(View.GONE);
        }

        /**
         * OnClickListener for confirm button.
         * Changes layout visibility.
         */
        buttonConfirm.setOnClickListener(
            new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutConfirmWait.setVisibility(View.VISIBLE);
                layoutConfirmButtons.setVisibility(View.GONE);

                coreLogic.accept(new IDataCallback() {
                    @Override
                    public void run(Object rawData) {
                        Map<String, Object> data = (Map<String, Object>) rawData;
                        // set state
                        coreLogic.setState(UserState.valueOf((String) data.get(Constants.PROPERTY_STATE)));
                        // Hide controls
                        layoutConfirmWait.setVisibility(View.VISIBLE);
                        layoutConfirmButtons.setVisibility(View.GONE);

                    }
                },
                new IDataCallback() {
                    @Override
                    public void run(Object data) {
                        // Show controls again
                        layoutConfirmWait.setVisibility(View.GONE);
                        layoutConfirmButtons.setVisibility(View.VISIBLE);
                    }
                });
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
                                coreLogic.cancel(new IDataCallback() {
                                    @Override
                                    public void run(Object rawData) {
                                        Map<String, Object> data = (Map<String, Object>) rawData;
                                        // set state
                                        coreLogic.setState(UserState.valueOf((String) data.get(Constants.PROPERTY_STATE)));
                                    }
                                }, null);
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

        if (participantsListJson != null) {
            readParticipantJson(participantsListJson);
        }

        // disable static ConfirmationBackgroundReceiver
        ComponentName component = new ComponentName(this, ConfirmationBackgroundReceiver.class);
        getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        Log.d("GCM", "disabled confirmation background receiver");

        // register ConfirmationForegroundReceiver
        //LocalBroadcastManager.getInstance(this).registerReceiver(foregroundReceiver,
        //        new IntentFilter(ConfirmationActivity.BROADCAST_CONFIRMATION));

        registerReceiver(foregroundReceiver,
                new IntentFilter(ConfirmationActivity.BROADCAST_CONFIRMATION));

        coreLogic.initialize(null);
        updateLobby();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // unregister ConfirmationForegroundReceiver
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(foregroundReceiver);

        unregisterReceiver(foregroundReceiver);

        //enable static ConfirmationBackgroundReceiver
        ComponentName component = new ComponentName(this, ConfirmationBackgroundReceiver.class);
        getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Log.d("GCM", "re enabled static confirmation background receiver");


    }

    /**
     * Returns participants adapter
     *
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

        for (Object entry : jsonList) {
            Map<String, String> entryMap = (Map<String, String>) entry;
            String id = entryMap.get("id");
            String name = entryMap.get("name");
            String state = entryMap.get("state");

            Participant participant = new Participant(id, name, state);
            participantsAdapter.add(participant);
        }
    }

    private void updateLobby() {
        final Context context = this;
        coreLogic.getLobby(
                new IDataCallback() {
                    @Override
                    public void run(Object rawData) {
                        // onSuccess
                        final Map<String, Object> data = (Map<String, Object>) rawData;

                        if(data.keySet().contains(Constants.PROPERTY_STATE)) {
                            UserState state;
                            state = UserState.valueOf((String) data.get(Constants.PROPERTY_STATE));
                            coreLogic.setState(state);
                            return;
                        }
                        // TODO: update list
                        List<Object> participants = (List<Object>) data.get("participants");
                        Log.d("ConfirmationActivity", participants.toString());

                        final List<Participant> resultList = new ArrayList<Participant>();
                        for (Object rawEntry : participants) {
                            Map<String, Object> entry = (Map<String, Object>) rawEntry;
                            String id = (String) entry.get(Constants.PROPERTY_USER_ID);
                            String status = (String) entry.get(Constants.PROPERTY_STATE);
                            String name = (String) entry.get(Constants.PROPERTY_USER_NAME);
                            resultList.add(new Participant(id, name, status));
                        }
                        ConfirmationActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                participantsAdapter.emptySilent();
                                participantsAdapter.addAll(resultList);
                                final TextView tx = (TextView) findViewById(R.id.text_confirm_queue);
                                tx.setText((String)data.get("MATCH_TAG"));
                            }
                        });
                    }
                },
                new IDataCallback() {
                    @Override
                    public void run(final Object data) {
                        // onError
                        Handler mainHandler = new Handler(context.getMainLooper());

                        Runnable runOnUI = new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, data.toString(), Toast.LENGTH_LONG).show();
                            }
                        };
                        mainHandler.post(runOnUI);
                    }
                }
        );


    }

}
