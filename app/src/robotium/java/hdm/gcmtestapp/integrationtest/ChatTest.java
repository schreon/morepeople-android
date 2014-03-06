package hdm.gcmtestapp.integrationtest;


import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import hdm.gcmtestapp.gcmtest.OverviewActivity;

public class ChatTest extends ActivityInstrumentationTestCase2<OverviewActivity> {
    private Solo solo;

    public ChatTest() {
        super(OverviewActivity.class);
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
}