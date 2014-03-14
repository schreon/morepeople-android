package morepeople.android.app;


import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import morepeople.android.app.SearchActivity;

public class SearchEnvironmentTest extends ActivityInstrumentationTestCase2<SearchActivity> {
    private Solo solo;

    public SearchEnvironmentTest() {
        super(SearchActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testActivity() throws Exception {
        // robotium assert
        // solo.assertCurrentActivity("Welcome Screen", OverviewActivity.class);
        // junit assert
        assertTrue(true);
    }

    public void testActivity2() throws Exception {
        // robotium assert
        // solo.assertCurrentActivity("Welcome Screen", OverviewActivity.class);
        // junit assert
        assertTrue(false);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
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