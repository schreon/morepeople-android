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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import morepeople.android.app.morepeople.android.app.core.CoreLocation;
import morepeople.android.app.morepeople.android.app.core.CoreLogic;
import morepeople.android.app.morepeople.android.app.core.ICoreLocation;
import morepeople.android.app.morepeople.android.app.core.ICoreLogic;
import morepeople.android.app.morepeople.android.app.core.IDataCallback;


/**
 * On this activity, the user sees the nearby searches of other users.
 * He can search them, join them or add an own search.
 */
public class SearchActivity extends Activity {

    private SearchAdapter searchAdapter;
    private ICoreLocation coreLocation;
    private IDataCallback onLocationUpdate;
    private ICoreLogic coreLogic;
    private Location userLocation;

    private EditText inputSearch;

    /**
     * @param savedInstanceState contains the previous state of the activity if it was existent before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchAdapter = new SearchAdapter();
        final ListView listView = (ListView) findViewById(R.id.list_search);
        listView.setAdapter(searchAdapter);
        final LinearLayout layoutAddSearch = (LinearLayout) findViewById(R.id.layout_add_search);
        final LinearLayout layoutWaiting = (LinearLayout) findViewById(R.id.layout_waiting);
        final LinearLayout layoutSearchInput = (LinearLayout) findViewById(R.id.layout_search_input);
        layoutAddSearch.setVisibility(View.GONE);
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
                    layoutAddSearch.setVisibility(View.VISIBLE);
                } else {
                    layoutAddSearch.setVisibility(View.GONE);
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

        Button buttonSendSearch = (Button) this.findViewById(R.id.button_send_search);
        /**
         * onClickListener for buttoSendSearch adds, searchentry and resets text and changes layout visibility
         */
        buttonSendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = inputSearch.getText().toString();
                searchAdapter.add(new SearchEntry("testid", inputSearch.getText().toString(), "Hans Dampf", "1/3"));
                inputSearch.setText("");
                layoutWaiting.setVisibility(View.VISIBLE);
                layoutSearchInput.setVisibility(View.GONE);
                coreLogic.queue(searchTerm,
                        new IDataCallback() {
                            @Override
                            public void run(Object data) {

                            }
                        },
                        new IDataCallback() {
                            @Override
                            public void run(Object data) {

                            }
                        }
                );
            }
        });

        coreLocation = new CoreLocation(this);

        ICoreLogic.UserState currentState = null;
        try {
            currentState = ICoreLogic.UserState.valueOf(getIntent().getExtras().getString(ICoreLogic.KEY_STATE));
        } catch (Exception e) {
            Log.e("SearchActivity", e.getMessage());
        }
        coreLogic = new CoreLogic(this, currentState);

        onLocationUpdate = new IDataCallback() {
            @Override
            public void run(Object rawData) {
                userLocation = (Location) rawData;
                searchAndUpdate();
            }
        };

        userLocation = null;
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
                                String creator = "-";
                                String participants = "-";
                                resultList.add(new SearchEntry(id, description, creator, participants));
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
        coreLocation.setPolling(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        coreLocation.setPolling(false);
        // TODO: stop poll
    }
}
