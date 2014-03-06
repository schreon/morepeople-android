package morepeople.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


/**
 * On this activity, the user sees the nearby searches of other users. He can search them, join them or add an own search.
 */
public class SearchActivity extends Activity {

    SearchAdapter searchAdapter;
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
    }

    public SearchAdapter getSearchAdapter() {
        // TODO
        return searchAdapter;
    }

    // TODO: remove search button (instant search) and add dynamic "add search" entry
}
