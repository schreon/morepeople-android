package morepeople.android.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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

    private SearchAdapter searchAdapter;
    private EditText inputSearch;

    /**
     * @param savedInstanceState contains the previous state of the activity if it was existent before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getActionBar().setTitle("morepeople");
    }

    @Override
    protected void onCoreInitFinished() {

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
            }
        });


        /**
         * onClickListener for buttoSendSearch adds, searchentry and resets text and changes layout visibility
         */
        buttonSendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = inputSearch.getText().toString();
                hideControls();
                coreApi.queue(searchTerm, null);
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
    }

    private void hideControls() {
        Handler mainHandler = new Handler(this.getMainLooper());

        Runnable runOnUI = new Runnable() {
            @Override
            public void run() {
                final View layoutWaiting = findViewById(R.id.layout_waiting);
                final View layoutSearchInput = findViewById(R.id.layout_search_input);
                layoutWaiting.setVisibility(View.VISIBLE);
                layoutSearchInput.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                inputSearch.setText("");
                imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
            }
        };
        mainHandler.post(runOnUI);

    }

    private void showControls() {
        Handler mainHandler = new Handler(this.getMainLooper());

        Runnable runOnUI = new Runnable() {
            @Override
            public void run() {
                final View layoutWaiting = findViewById(R.id.layout_waiting);
                final View layoutSearchInput = findViewById(R.id.layout_search_input);
                layoutWaiting.setVisibility(View.GONE);
                layoutSearchInput.setVisibility(View.VISIBLE);
            }
        };
        mainHandler.post(runOnUI);
    }

    private void adaptViewToState(UserState state) {
        // Hide the controls if already queued
        if (UserState.QUEUED.equals(state)) {
            hideControls();
        } else {
            showControls();
        }
    }

    private void searchAndUpdate() {
        String searchTerm = inputSearch.getText().toString();
        if (searchTerm.isEmpty()) {
            searchTerm = null;
        }
        coreApi.search(
                coreApi.getPreferences().getLastKnownCoordinates(),
                1000,
                searchTerm,
                defaultErrorCallback
        );
    }

    private void updateListView() {
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
        updateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // do a search
        searchAndUpdate();
        updateListView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
