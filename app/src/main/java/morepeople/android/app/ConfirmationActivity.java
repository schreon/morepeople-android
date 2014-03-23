package morepeople.android.app;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.structures.Participant;
import morepeople.android.app.structures.UserState;

/**
 * ConfirmationActivity is shown if enough users were found to start a match.
 */
public class ConfirmationActivity extends BaseActivity {

    private BroadcastReceiver foregroundReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("GCM", "ConfirmationActivity.foregroundReceiver");
            coreApi.getLobby(defaultErrorCallback);
        }
    };

    private ParticipantsAdapter participantsAdapter;
    private LinearLayout layoutConfirmWait;
    private LinearLayout layoutConfirmButtons;

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
    }

    @Override
    protected void onCoreInitFinished() {

        participantsAdapter = new ParticipantsAdapter();

        ListView listView = (ListView) this.findViewById(R.id.confirm_list_view);
        listView.setAdapter(participantsAdapter);

        layoutConfirmWait = (LinearLayout) this.findViewById(R.id.confirm_wait_layout);
        layoutConfirmButtons = (LinearLayout) this.findViewById(R.id.confirm_button_layout);
        Button buttonConfirm = (Button) this.findViewById(R.id.button_confirm);

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

                        coreApi.accept(defaultErrorCallback);
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
                                coreApi.cancel(defaultErrorCallback);
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

        updateParticipantList();
        updateLobby();
    }

    private Runnable delayedLobby = new Runnable() {
        @Override
        public void run() {
            updateLobby();
        }
    };

    private void adaptViewToState(UserState state) {
        // Hide the controls if already queued
        if (UserState.ACCEPTED.equals(state)) {
            hideControls();
        } else {
            showControls();
        }
    }

    private void hideControls() {
        layoutConfirmWait.setVisibility(View.VISIBLE);
        layoutConfirmButtons.setVisibility(View.GONE);
    }

    private void showControls() {
        layoutConfirmWait.setVisibility(View.GONE);
        layoutConfirmButtons.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // disable static ConfirmationBackgroundReceiver
        ComponentName component = new ComponentName(this, ConfirmationBackgroundReceiver.class);
        getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        Log.d("GCM", "disabled confirmation background receiver");

        // register ConfirmationForegroundReceiver
        //LocalBroadcastManager.getInstance(this).registerReceiver(foregroundReceiver,
        //        new IntentFilter(ConfirmationActivity.BROADCAST_LOCAL_CONFIRMATION));

        registerReceiver(foregroundReceiver,
                new IntentFilter(Constants.BROADCAST_LOCAL_CONFIRMATION));

        if (coreApi != null) {
            adaptViewToState(coreApi.getPreferences().getCurrentUserState());
        }
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

        mainHandler.removeCallbacks(delayedLobby);

        Log.d("GCM", "re enabled static confirmation background receiver");
    }

    private void updateParticipantList() {
        final List<Participant> participants = coreApi.getPreferences().getParticipantList();
        ConfirmationActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                participantsAdapter.emptySilent();
                participantsAdapter.addAll(participants);
                final TextView tx = (TextView) findViewById(R.id.text_confirm_queue);
                tx.setText(coreApi.getPreferences().getMatchTag());
            }
        });
    }

    private void updateLobby() {
        coreApi.getLobby(defaultErrorCallback);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (coreApi != null) {
            updateParticipantList();
            mainHandler.postDelayed(delayedLobby, 10000);
        }
    }
}
