package morepeople.android.app;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.util.HashMap;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by schreon on 3/6/14.
 */
@SuppressWarnings("ConstantConditions")
@Config(emulateSdk = 18)
@RunWith(TestRunner.class)
public class SearchActivityTest {
    SearchActivity activity;
    /**
     * Setup method
     */
    @Before
    public void setUp(){
        activity = Robolectric.buildActivity(SearchActivity.class).create().get();
    }

    @Test
    public void activityShouldNotBeNull() {
        assertNotNull(activity);
    }

    // should have a list of search results
    @Test
    public void shouldHaveSearchView() {
        ListView searchView = (ListView) activity.findViewById(R.id.list_search);
        assertNotNull(searchView);
    }

    // TODO: when search results have been found, they should be displayed in the search view
    @Test
    public void shouldDisplaySearchEntries() {
        ListView searchView = (ListView)activity.findViewById(R.id.list_search);
        SearchAdapter searchAdapter = activity.getSearchAdapter();

        // add some search entries
        for (int i=0; i <20; i++) {
            SearchEntry searchEntry = new SearchEntry(String.valueOf(i), "schmusen", "Hans Dampf", "1/3");
            searchAdapter.addEntry(searchEntry);

            // update robolectric
            Robolectric.shadowOf(searchView).populateItems();

            // searchVview should have at least 1 child now
            assertTrue(searchView.getChildCount() > 0);

            boolean isDisplayed = false;
            // the ListView should now display the new message
            for (int j=0; j < searchView.getChildCount(); j++) {
                LinearLayout group = (LinearLayout)searchView.getChildAt(j);

                SearchEntry foundSearchEntry = (SearchEntry) group.getTag();
                String foundId = foundSearchEntry.id;

                String foundDescription = ((TextView)group.getChildAt(0)).getText().toString();
                String foundCreator = ((TextView)group.getChildAt(1)).getText().toString();
                String foundParticipants = ((TextView)group.getChildAt(2)).getText().toString();

                if (foundId != searchEntry.id) continue;
                if (foundDescription != searchEntry.description) continue;
                if (foundCreator != searchEntry.creator) continue;
                if (foundParticipants != searchEntry.participants) continue;

                isDisplayed = true;
            }
            assertTrue(isDisplayed);
        }
    }
}
