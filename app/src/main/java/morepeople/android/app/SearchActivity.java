package morepeople.android.app;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import morepeople.android.app.core.CoreLocationManager;
import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.UserState;
import morepeople.android.app.morepeople.android.app.core.CoreAPI;
import morepeople.android.app.interfaces.ICoreLocationManager;
import morepeople.android.app.interfaces.IDataCallback;


/**
 * On this activity, the user sees the nearby searches of other users.
 * He can search them, join them or add an own search.
 */
public class SearchActivity extends Activity {

    private SearchAdapter searchAdapter;
    private ICoreLocationManager coreLocation;
    private IDataCallback onLocationUpdate;
    private ICoreApi coreLogic;
    private Location userLocation;

    private EditText inputSearch;

    /**
     * @param savedInstanceState contains the previous state of the activity if it was existent before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getActionBar().setTitle("morepeople");
        coreLocation = new CoreLocationManager(this);
        UserState currentState = null;
        try {
            currentState = UserState.valueOf(getIntent().getExtras().getString(Constants.PROPERTY_STATE));
        } catch (Exception e) {
            Log.e("SearchActivity", e.getMessage());
        }
        coreLogic = new CoreAPI(this, currentState);

        userLocation = null;
        onLocationUpdate = new IDataCallback() {
            @Override
            public void run(Object rawData) {
                userLocation = (Location) rawData;
                // search and refresh state
                searchAndUpdate();
                coreLogic.initialize(null);
            }
        };
        searchAdapter = new SearchAdapter(coreLogic);
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
                coreLogic.queue(searchTerm,
                        new IDataCallback() {
                            @Override
                            public void run(Object data) {
                                searchAndUpdate();
                            }
                        }, null
                );
            }
        });

        // Hide the controls if already queued
        if (UserState.QUEUED.equals(currentState)) {
            hideControls();
        } else {
            showControls();
        }

        searchAdapter.setOnQueueSuccess(new IDataCallback() {
            @Override
            public void run(Object rawData) {
                hideControls();
                Map<String, Object> data = (Map<String, Object>) rawData;
                // set state
                coreLogic.setState(UserState.valueOf((String) data.get(Constants.PROPERTY_STATE)));
                searchAndUpdate();
            }
        });

        Button buttonCancelSearch = (Button) this.findViewById(R.id.button_cancel_search);
        /**
         * onClickListener for buttoSendSearch adds, searchentry and resets text and changes layout visibility
         */
        buttonCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coreLogic.cancel(
                        new IDataCallback() {
                            @Override
                            public void run(Object rawData) {
                                Map<String, Object> data = (Map<String, Object>) rawData;
                                // set state
                                UserState state = UserState.valueOf((String) data.get(Constants.PROPERTY_STATE));
                                adaptViewToState(state);
                                coreLogic.setState(state);
                            }
                        },
                        null
                );
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
                InputMethodManager imm = (InputMethodManager)getSystemService(
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
        // TODO: deal with concurrency in the right way (with a semaphore for example)
        if (userLocation != null) {
            String searchTerm = inputSearch.getText().toString();
            if (searchTerm.isEmpty()) {
                searchTerm = null;
            }
            final Context context = this;
            coreLogic.search(userLocation, 1000, searchTerm, new IDataCallback() {
                        @Override
                        public void run(Object rawData) {
                            // onSuccess
                            Map<String, Object> data = (Map<String, Object>) rawData;
                            // TODO: update list
                            List<Object> results = (List<Object>) data.get("results");
                            Log.d("SearchActivity", results.toString());

                            final List<SearchEntry> resultList = new ArrayList<SearchEntry>();
                            for (Object entry : results) {
                                Map<String, Object> res = (Map<String, Object>) entry;
                                String description = (String) res.get("MATCH_TAG");
                                String id = (String) res.get("USER_ID");
                                String creator = (String) res.get("USER_NAME");
                                resultList.add(new SearchEntry(id, description, creator));
                            }
                            SearchActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    searchAdapter.emptySilent();
                                    searchAdapter.addAll(resultList);
                                }
                            });
                        }
                    }, new IDataCallback() {
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

    /**
     * Get searchAdapter
     *
     * @return searchAdapter
     */
    public SearchAdapter getSearchAdapter() {
        // TODO
        return searchAdapter;
    }

    // TODO: remove search button (instant search) and add dynamic "add search" entry

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: poll search
        coreLocation.setLocationUpdateHandler(onLocationUpdate);
        coreLocation.setListenToLocationUpdates(true);
        // do a search
        searchAndUpdate();
        // update status
        coreLogic.initialize(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        coreLocation.setListenToLocationUpdates(false);
        // TODO: stop poll
    }
}
