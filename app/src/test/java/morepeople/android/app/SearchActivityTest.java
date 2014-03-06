package morepeople.android.app;

import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;

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

    // should have a list of texts where the chat history is shown
    @Test
    public void shouldHaveChatHistory() {
        ListView searchView = (ListView) activity.findViewById(R.id.list_search);
        assertNotNull(searchView);
    }
}
