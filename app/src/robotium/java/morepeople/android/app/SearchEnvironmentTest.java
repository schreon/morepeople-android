package morepeople.android.app;


import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

import org.json.JSONObject;

import java.util.List;

public class SearchEnvironmentTest extends IntegrationTest<WelcomeActivity> {
    private Solo solo;

    public SearchEnvironmentTest() {
        super(WelcomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        doGetRequest("/reset");
    }
//
//    public void testNoItemsInListIfReseted() throws Exception {
//        solo = new Solo(getInstrumentation(), getActivity());
//
//        ListView listView = solo.getCurrentViews(ListView.class).get(0);
//        List<TextView> textViewsInList = solo.getCurrentViews(TextView.class, listView);
//
//        // there should be only one item in the list
//        assertEquals(0, textViewsInList.size());
//
//        // finish activity
//        solo.finishOpenedActivities();
//    }

    public void testCancelSearchedEntry() throws Exception {
        // robotium assert
        // solo.assertCurrentActivity("Welcome Screen", OverviewActivity.class);
        // junit assert
        Log.d("robotium", "testCancelSearchedEntry");

        // 1. other user searches for event
        String userIdentifier = "test_user_"+System.currentTimeMillis();
        JSONObject userJson = new JSONObject();
        userJson.put("USER_ID", userIdentifier);
        userJson.put("USER_NAME", userIdentifier);
        JSONObject locJson = new JSONObject();
        locJson.put("LONGITUDE", 9.1+0.1*Math.random());
        locJson.put("LATITUDE", 48.7+0.1*Math.random());
        userJson.put("LOC", locJson);
        userJson.put("MATCH_TAG", "bier");
        userJson.put("TIME_LEFT", 1000);

        doPostRequest("/queue", userJson);

        Log.d("robotium", "creating solo");
        solo = new Solo(getInstrumentation(), getActivity());

        solo.waitForActivity(SearchActivity.class);

        Log.d("robotium", "getting listview");
        final ListView listView = solo.getCurrentViews(ListView.class).get(0);

        Log.d("robotium", "wait for listview to populate");
        solo.waitForCondition(new Condition() {
            @Override
            public boolean isSatisfied() {
                return listView.getChildCount() > 0;
            }
        }, 10000);

        Log.d("robotium", "assert that exactly 1 entry is in listview");
        // there should be only one item in the list
        assertEquals(1, listView.getChildCount());

        solo.clickInList(0);

        // the confirmation dialog appears
        solo.waitForDialogToOpen();

        // click on the confirm button
        solo.clickOnButton("Ja");

        // the confirmation dialog closes
        solo.waitForDialogToClose();

//        // check if there is a view with the text "you are waiting"
//        assertTrue(solo.searchText("you are waiting"));
//
//        // there should be only one item in the list
//        assertEquals(2, listView.getChildCount());
//
//        // click on cancel button
//        solo.clickOnButton("cancel");
//
//        // the confirmation dialog appears
//        solo.waitForDialogToOpen();
//
//        // click on confirm button
//        solo.clickOnButton("yes");
//
//        // the confirmation dialog closes
//        solo.waitForDialogToClose();
//
//        // there should be no view with the text "you are waiting"
//        assertFalse(solo.searchText("you are waiting"));

    }

    @Override
    public void tearDown() throws Exception {
        // finish activity
        solo.finishOpenedActivities();

        super.tearDown();
    }

    // 1. start app
    // 2. expected: see searches of 1 nearby user ( with same same search tag )
    // 3. click on one of the search entries
    // 4. expected: pop up opens, asking if I want to search for the same thing
    // 5. click on positive button
    // 6. expected: "you are waiting" layout is visible, search bar is "gone", my search entry appears in the lsit of searches
    // 7. click on "cancel search"
    // 8. expected: pop up, asking if I really want to cancel
    // 9. click on positive button
    // 10. search bar appears again, "you are waiting" layout is gone, my search entry is gone
    // 11. type in the same search as the other user
    // 12. expected: "create new search" appears
    // 13. click "create new search"
    // 14. expected: "create new search" disappears, "you are waiting" appears, search entry appears
    // 15. second mock user is added, searching for the same
    // 16. expected: transition to confirm-activity
    // 17. click on "accept"
    // 18. expected: buttons disappear, my entry indicates "accepted"
    // 19. one of the mock users accepts
    // 20. expected: his entry in the list indicates "accepted"
    // 21. second mock user accepts
    // 22. expected: transition to chat activity
    // 23. ...

}