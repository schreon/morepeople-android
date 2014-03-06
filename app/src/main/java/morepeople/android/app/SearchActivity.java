package morepeople.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;


/**
 * On this activity, the user sees the nearby searches of other users. He can search them, join them or add an own search.
 */
public class SearchActivity extends Activity {

    private SearchAdapter searchAdapter;
    /**
     * @param savedInstanceState contains the previous state of the activity if it was existent before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchAdapter = new SearchAdapter();
        ListView listView = (ListView) findViewById(R.id.list_search);
        listView.setAdapter(searchAdapter);
        final LinearLayout layoutAddSearch = (LinearLayout) findViewById(R.id.layout_add_search);
        layoutAddSearch.setVisibility(View.INVISIBLE);

        final EditText inputSearch = (EditText) this.findViewById(R.id.input_search);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(charSequence.length() > 0) {
                    layoutAddSearch.setVisibility(View.VISIBLE);
                } else {

                    layoutAddSearch.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button buttonSendSearch = (Button) this.findViewById(R.id.button_send_search);
        buttonSendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAdapter.add(new SearchEntry("testid", inputSearch.getText().toString(), "Hans Dampf", "1/3"));
                inputSearch.setText("");
            }
        });
    }

    public SearchAdapter getSearchAdapter() {
        // TODO
        return searchAdapter;
    }

    // TODO: remove search button (instant search) and add dynamic "add search" entry
}
