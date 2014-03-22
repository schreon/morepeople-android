package morepeople.android.app;

import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;

import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.ICoreRegistrar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertSame;

/**
 * Created by schreon on 3/3/14.
 */

@SuppressWarnings("ConstantConditions")
@Config(emulateSdk = 18)
@RunWith(TestRunner.class)
/**
 * Created by schreon on 3/4/14.
 */
public class ChatAdapterTest {
    ChatAdapter chatAdapter;
    ChatActivity activity;

    public static void sharedPrefs() {
        // Insert registration id and the user name into SharedPreferences
        SharedPreferences sharedPreferences = Robolectric.application.getSharedPreferences(ICoreApi.SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("appUsername", "Thorsten Test").commit();
        sharedPreferences.edit().putString(ICoreRegistrar.PROPERTY_REG_ID, "test-gcm-id").commit();
    }

    /**
     * Setup method
     */
    @Before
    public void setUp(){
        sharedPrefs();
        activity = Robolectric.buildActivity(ChatActivity.class).create().get();
        chatAdapter = new ChatAdapter();
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(chatAdapter);
    }

    // if a user sends a chat message, it should be contained in the list of all messages
    @Test
    public void shouldContainNewMessages() {
        assertNotNull(chatAdapter);

        // new message arrives
        String testMessage = "This is a test Message!";
        chatAdapter.addNewMessage(testMessage);

        assertTrue(chatAdapter.contains(testMessage));
    }

    @Test
    public void shouldBehaveLikeListAdapter() {
        assertEquals(chatAdapter.getCount(), 0);
        String testMessage = "Test message";
        chatAdapter.addNewMessage(testMessage);
        assertEquals(chatAdapter.getCount(), 1);
        assertEquals(chatAdapter.getItem(0), testMessage);
        assertEquals(chatAdapter.getItemId(0), 0);
    }

    @Test
    public void shouldReturnTextViews() {
        String testMessage = "Test message";
        chatAdapter.addNewMessage(testMessage);
        TextView messageView = (TextView) chatAdapter.getView(0, null, (ViewGroup) activity.findViewById(R.id.chat_history));
        assertNotNull(messageView);
        assertEquals(messageView.getText().toString(), testMessage);
    }

    @Test
    public void shouldReuseTextView() {
        String testMessage1 = "Test message";
        chatAdapter.addNewMessage(testMessage1);
        String testMessage2 = "Test message";
        chatAdapter.addNewMessage(testMessage2);
        TextView messageView1 = (TextView) chatAdapter.getView(0, null, (ViewGroup) activity.findViewById(R.id.chat_history));
        assertNotNull(messageView1);
        assertEquals(messageView1.getText().toString(), testMessage1);
        TextView messageView2 = (TextView) chatAdapter.getView(1, messageView1, (ViewGroup) activity.findViewById(R.id.chat_history));
        assertNotNull(messageView2);
        assertEquals(messageView1.getText().toString(), testMessage2);
        assertSame(messageView1, messageView2);
    }

    @Test(expected=RuntimeException.class)
    public void shouldRejectMissingViewGroup() {
        chatAdapter.getView(0, null, null);
    }

    @Test(expected=RuntimeException.class)
    public void shouldRejectBrokenViewGroup() {
        ViewGroup myViewGroup = new ViewGroup(null) {
            @Override
            protected void onLayout(boolean b, int i, int i2, int i3, int i4) {

            }
        };
        chatAdapter.getView(0, null, myViewGroup);
    }
    @Test(expected=RuntimeException.class)
    public void shouldRejectMissingMessage() {
        chatAdapter.addNewMessage(null);
    }

    @Test(expected=RuntimeException.class)
    public void shouldRejectEmptyMessage() {
        chatAdapter.addNewMessage("");
    }
}
