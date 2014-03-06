package hdm.gcmtestapp.gcmtest;

import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertSame;

/**
 * Created by schreon on 3/3/14.
 */

@SuppressWarnings("ConstantConditions")
@Config(emulateSdk = 18)
@RunWith(hdm.gcmtestapp.gcmtest.TestRunner.class)
/**
 * Created by schreon on 3/4/14.
 */
public class ChatHistoryTest {
    ChatHistory chatHistory;
    OverviewActivity overviewActivity;
    /**
     * Setup method
     */
    @Before
    public void setUp(){
        overviewActivity = Robolectric.buildActivity(OverviewActivity.class).create().get();
        chatHistory = new ChatHistory();
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(chatHistory);
    }

    // if a user sends a chat message, it should be contained in the list of all messages
    @Test
    public void shouldContainNewMessages() {
        assertNotNull(chatHistory);

        // TODO: the following should work repeatedly

        // new message arrives
        String testMessage = "This is a test Message!";
        chatHistory.addNewMessage(testMessage);

        assertTrue(chatHistory.contains(testMessage));
    }

    @Test
    public void shouldBehaveLikeListAdapter() {
        assertEquals(chatHistory.getCount(), 0);
        String testMessage = "Test message";
        chatHistory.addNewMessage(testMessage);
        assertEquals(chatHistory.getCount(), 1);
        assertEquals(chatHistory.getItem(0), testMessage);
        assertEquals(chatHistory.getItemId(0), 0);
    }

    @Test
    public void shouldReturnTextViews() {
        String testMessage = "Test message";
        chatHistory.addNewMessage(testMessage);
        TextView messageView = (TextView) chatHistory.getView(0, null, (ViewGroup)overviewActivity.findViewById(R.id.chat_history));
        assertNotNull(messageView);
        assertEquals(messageView.getText().toString(), testMessage);
    }

    @Test
    public void shouldReuseTextView() {
        String testMessage1 = "Test message";
        chatHistory.addNewMessage(testMessage1);
        String testMessage2 = "Test message";
        chatHistory.addNewMessage(testMessage2);
        TextView messageView1 = (TextView) chatHistory.getView(0, null, (ViewGroup)overviewActivity.findViewById(R.id.chat_history));
        assertNotNull(messageView1);
        assertEquals(messageView1.getText().toString(), testMessage1);
        TextView messageView2 = (TextView) chatHistory.getView(1, messageView1, (ViewGroup)overviewActivity.findViewById(R.id.chat_history));
        assertNotNull(messageView2);
        assertEquals(messageView1.getText().toString(), testMessage2);
        assertSame(messageView1, messageView2);
    }

    @Test(expected=RuntimeException.class)
    public void shouldRejectMissingViewGroup() {
        chatHistory.getView(0, null, null);
    }

    @Test(expected=RuntimeException.class)
    public void shouldRejectBrokenViewGroup() {
        ViewGroup myViewGroup = new ViewGroup(null) {
            @Override
            protected void onLayout(boolean b, int i, int i2, int i3, int i4) {

            }
        };
        chatHistory.getView(0, null, myViewGroup);
    }
    @Test(expected=RuntimeException.class)
    public void shouldRejectMissingMessage() {
        chatHistory.addNewMessage(null);
    }

    @Test(expected=RuntimeException.class)
    public void shouldRejectEmptyMessage() {
        chatHistory.addNewMessage("");
    }
}
