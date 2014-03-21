package morepeople.android.app;


import android.util.Log;
import android.widget.ListView;

import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

import org.json.JSONObject;

public class SearchEnvironmentTest extends BaseIntegrationTest<WelcomeActivity> {
    private Solo solo;

    public SearchEnvironmentTest() {
        super(WelcomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Log.d("robotium", "reset test server state.");
        doGetRequest("/reset");

        Log.d("robotium", "creating solo");
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void addTestUser(String search_tag) throws Exception {
        String userIdentifier = "test_user_"+System.currentTimeMillis();
        JSONObject userJson = new JSONObject();
        userJson.put("USER_ID", userIdentifier);
        userJson.put("USER_NAME", userIdentifier);
        JSONObject locJson = new JSONObject();
        locJson.put("LONGITUDE", 9.1+0.1*Math.random());
        locJson.put("LATITUDE", 48.7+0.1*Math.random());
        userJson.put("LOC", locJson);
        userJson.put("MATCH_TAG", search_tag);
        userJson.put("TIME_LEFT", 1000);
        doPostRequest("/queue", userJson);
    }

    public void testNoItemsInListIfResetted() throws Exception {
        Log.d("robotium", "testNoItemsInListIfResetted");

        Log.d("robotium", "wait, until the search activity shows up");
        solo.waitForActivity(SearchActivity.class);
        solo.waitForActivity(solo.getCurrentActivity().toString());

        Log.d("robotium", "get the listview");
        ListView listView = solo.getCurrentViews(ListView.class).get(0);

        Log.d("robotium", "test that the list view is empty");
        assertEquals(0, listView.getChildCount());
    }

    public void testSearchAndClickAndCancel() throws Exception {
        Log.d("robotium", "testSearchAndClickAndCancel");

        Log.d("robotium", "add test user");
        addTestUser("bier");

        Log.d("robotium", "wait, until the search activity shows up");
        solo.waitForActivity(SearchActivity.class);
        solo.waitForActivity(solo.getCurrentActivity().toString());

        Log.d("robotium", "get the listview");
        final ListView listView = solo.getCurrentViews(ListView.class).get(0);

        Log.d("robotium", "wait for listview to populate, with a timeout of 60 seconds");
        solo.waitForCondition(new Condition() {
            @Override
            public boolean isSatisfied() {
                return listView.getChildCount() > 0;
            }
        }, 60000);

        Log.d("robotium", "assert that exactly 1 entry is in listview");
        assertEquals(1, listView.getChildCount());

        Log.d("robotium", "click first item in the list");
        solo.clickInList(0);

        Log.d("robotium", "wait for the confirmation dialog to appear");
        solo.waitForDialogToOpen();

        Log.d("robotium", "click on the cancel button");
        solo.clickOnButton("Nein");

        Log.d("robotium", "wait for the dialog to close");
        solo.waitForDialogToClose();
    }

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