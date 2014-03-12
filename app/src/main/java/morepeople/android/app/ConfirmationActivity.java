package morepeople.android.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * ConfirmationActivity is shown if enough users were found to start an activity
 */
public class ConfirmationActivity extends Activity {

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

    /**
     * Returns participants adapter
     * @return participantsAdapter
     */
    public ParticipantsAdapter getParticipantsAdapter() {
        return participantsAdapter;
    }
}
