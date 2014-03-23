package morepeople.android.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import morepeople.android.app.structures.SearchEntry;
import morepeople.android.app.structures.UserState;


/**
 * On this activity, the user sees the nearby searches of other users.
 * He can search them, join them or add an own search.
 */
public class SearchActivity extends BaseActivity {
    private static final String TAG = "morepeople.android.app.SearchActivity";

    private SearchAdapter searchAdapter;
    private EditText inputSearch;

    /**
     * @param savedInstanceState contains the previous state of the activity if it was existent before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate started");
        Log.d(TAG, "onCreate finished");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCoreInitFinished() {
        Log.d(TAG, "starting onCoreInitFinished");
        searchAdapter = new SearchAdapter();
        final ListView listView = (ListView) findViewById(R.id.list_search);
        listView.setAdapter(searchAdapter);

        final Button buttonSendSearch = (Button) this.findViewById(R.id.button_send_search);
        inputSearch = (EditText) this.findViewById(R.id.input_search);
        /**
         * addtextchangedListener for inputSearch provides methods for text change
         */
        inputSearch.addTextChangedListener(new TextWatcher() {
            /**
             * What happens before text changed
             * @param charSequence
             * @param i
             * @param i2
             * @param i3
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            /**
             * What happens if text gets changed
             * Set layoutAddSearch to visible if textinput is not empty
             * @param charSequence
             * @param i
             * @param i2
             * @param i3
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Log.d(TAG, "onTextChanged");
                if (charSequence.length() > 0) {
                    buttonSendSearch.setVisibility(View.VISIBLE);
                } else {
                    buttonSendSearch.setVisibility(View.GONE);
                }
            }

            /**
             * What happens after text has been changed
             * @param editable
             */
            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged");
            }
        });


        /**
         * onClickListener for buttoSendSearch adds, searchentry and resets text and changes layout visibility
         */
        buttonSendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = inputSearch.getText().toString();
                inputSearch.setText("");
                hideControls();
                coreApi.queue(searchTerm, defaultErrorCallback);
            }
        });


        // Hide the controls if already queued
        if (UserState.QUEUED.equals(coreApi.getPreferences().getCurrentUserState())) {
            hideControls();
        } else {
            showControls();
        }

        Button buttonCancelSearch = (Button) this.findViewById(R.id.button_cancel_search);
        /**
         * onClickListener for buttoSendSearch adds, searchentry and resets text and changes layout visibility
         */
        buttonCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coreApi.cancel(defaultErrorCallback);
            }
        });

        searchAdapter.setOnSearchEntryClickListener(onSearchEntryClickListener);
        updateListView();
        Log.d(TAG, "finished onCoreInitFinished");
    }

    private View.OnClickListener onSearchEntryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final SearchEntry entry = (SearchEntry) view.getTag();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SearchActivity.this);

            // set title
            alertDialogBuilder.setTitle("Beitreten");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Möchtest Du \"" + entry.MATCH_TAG + "\" beitreten?")
                    .setCancelable(false)
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // enqueue for the same match tag
                            coreApi.queue(entry.MATCH_TAG, defaultErrorCallback);
                        }
                    })
                    .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
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
    };

    private void hideControls() {
        Runnable runOnUI = new Runnable() {
            @Override
            public void run() {
                final View layoutWaiting = findViewById(R.id.layout_waiting);
                final View layoutSearchInput = findViewById(R.id.layout_search_input);
                layoutWaiting.setVisibility(View.VISIBLE);
                layoutSearchInput.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
            }
        };
        this.runOnUiThread(runOnUI);
    }

    private void showControls() {
        Runnable runOnUI = new Runnable() {
            @Override
            public void run() {
                final View layoutWaiting = findViewById(R.id.layout_waiting);
                final View layoutSearchInput = findViewById(R.id.layout_search_input);
                layoutWaiting.setVisibility(View.GONE);
                layoutSearchInput.setVisibility(View.VISIBLE);
            }
        };
        this.runOnUiThread(runOnUI);
    }

    private void adaptViewToState(UserState state) {
        // Hide the controls if already queued
        if (UserState.QUEUED.equals(state)) {
            hideControls();
        } else {
            showControls();
        }
    }

    @Override
    protected synchronized void onPoll() {
        Log.d(TAG, "onPoll");
        if (coreApi != null) {
            coreApi.search(
                    coreApi.getPreferences().getLastKnownCoordinates(),
                    1000,
                    coreApi.getPreferences().getMatchTag(),
                    defaultErrorCallback
            );
        }
    }

    private void updateListView() {
        Log.d(TAG, "updateListView");

        final List<SearchEntry> searchEntries = coreApi.getPreferences().getSearchEntryList();
        SearchActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchAdapter.emptySilent();
                searchAdapter.addAll(searchEntries);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        if (coreApi != null) {
            // do a search
            updateListView();
            adaptViewToState(coreApi.getPreferences().getCurrentUserState());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (coreApi != null) {
            // do a search
            updateListView();
            adaptViewToState(coreApi.getPreferences().getCurrentUserState());
        } else {
            Log.d(TAG, "coreApi still null, doing nothing");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }
}
