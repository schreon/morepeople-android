package morepeople.android.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.ICoreRegistrar;
import morepeople.android.app.structures.SearchEntry;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by schreon on 3/6/14.
 */
@SuppressWarnings("ConstantConditions")
@Config(emulateSdk = 18)
@RunWith(TestRunner.class)
public class SearchActivityTest {
    SearchActivity activity;

    public static void sharedPrefs() {
        // Insert registration USER_ID and the user USER_NAME into SharedPreferences
        SharedPreferences sharedPreferences = Robolectric.application.getSharedPreferences(ICoreApi.SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("appUsername", "Thorsten Test").commit();
        sharedPreferences.edit().putString(ICoreRegistrar.PROPERTY_REG_ID, "test-gcm-USER_ID").commit();
    }

    /**
     * Setup method
     */
    @Before
    public void setUp(){
        sharedPrefs();
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
        for (int i=0; i < 40; i++) {
            SearchEntry searchEntry = new SearchEntry(String.valueOf(i), "schmusen", "Hans Dampf");
            searchAdapter.add(searchEntry);

            // update robolectric
            Robolectric.shadowOf(searchView).populateItems();

            // searchVview should have at least 1 child now
            assertTrue(searchView.getChildCount() > 0);

            boolean isDisplayed = false;
            // the ListView should now display the new message
            for (int j=0; j < searchView.getChildCount(); j++) {
                LinearLayout group = (LinearLayout)searchView.getChildAt(j);

                SearchEntry foundSearchEntry = (SearchEntry) group.getTag();

                assertTrue(group.getChildCount() > 0);

                String foundDescription = ((TextView)group.getChildAt(0)).getText().toString();
                String foundCreator = ((TextView)group.getChildAt(1)).getText().toString();

                if (!foundSearchEntry.USER_ID.equals(searchEntry.USER_ID)) continue;
                if (!foundDescription.equals(searchEntry.MATCH_TAG)) continue;
                if (!foundCreator.equals(searchEntry.USER_NAME)) continue;

                isDisplayed = true;
            }
            assertTrue(isDisplayed);
        }
    }

    // should display the "add search" button when the user has entered text
    // should add new search if the button is pressed
    // TODO: should display wait-view if search is submitted
    @Test
    public void shouldDisplayAddSearchButton() {
        final View layoutAddSearch = activity.findViewById(R.id.button_send_search);
        final View layoutSearchInput = activity.findViewById(R.id.layout_search_input);
        final View layoutWaiting = activity.findViewById(R.id.layout_waiting);

        EditText inputSearch = (EditText) activity.findViewById(R.id.input_search);

        assertEquals(layoutAddSearch.getVisibility(), View.GONE);
        inputSearch.setText("schmu");
        assertEquals(layoutAddSearch.getVisibility(), View.VISIBLE);
        inputSearch.setText("");
        assertEquals(layoutAddSearch.getVisibility(), View.GONE);

        String expectedDescription = "schmu";
        String expectedCreator = "Hans Dampf";
        String expectedParticipants = "1/3";

        inputSearch.setText(expectedDescription);

        assertEquals(layoutSearchInput.getVisibility(), View.VISIBLE);
        assertEquals(layoutWaiting.getVisibility(), View.GONE);
        Button buttonSendSearch = (Button) activity.findViewById(R.id.button_send_search);
        buttonSendSearch.performClick();
        assertTrue(inputSearch.getText().length() == 0);
        assertEquals(layoutAddSearch.getVisibility(), View.GONE);
        assertEquals(layoutSearchInput.getVisibility(), View.GONE);
        assertEquals(layoutWaiting.getVisibility(), View.VISIBLE);

        ListView searchView = (ListView)activity.findViewById(R.id.list_search);

        // update robolectric
        Robolectric.shadowOf(searchView).populateItems();
        boolean isDisplayed = false;
        // the ListView should now display the new message
        for (int j=0; j < searchView.getChildCount(); j++) {
            LinearLayout group = (LinearLayout)searchView.getChildAt(j);

            assertTrue(group.getChildCount() > 0);

            String foundDescription = ((TextView)group.getChildAt(0)).getText().toString();
            String foundCreator = ((TextView)group.getChildAt(1)).getText().toString();
            String foundParticipants = ((TextView)group.getChildAt(2)).getText().toString();

            if (!foundDescription.equals(expectedDescription)) continue;
            if (!foundCreator.equals(expectedCreator)) continue;
            if (!foundParticipants.equals(expectedParticipants)) continue;

            isDisplayed = true;
        }
        assertTrue(isDisplayed);

        // TODO: click list item

        // TODO: assert that a pop up is shown, asking the user if he wants to join

        // TODO: if the user is already waiting for a match, ask him if he wants to switch

    }

    /**
     * TODO: If the phone is active, it should poll the server for nearby events
     */
    @Test
    public void shouldPollEnvironment() {

    }

}
