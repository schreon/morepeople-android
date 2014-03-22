package morepeople.android.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.ICoreRegistrar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by schreon on 3/3/14.
 */

@SuppressWarnings("ConstantConditions")
@Config(emulateSdk = 18)
@RunWith(TestRunner.class)
public class ChatActivityTest {
    private ChatActivity activity;

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
    }

    @Test
    public void activityShouldNotBeNull() {
        assertNotNull(activity);
    }

    // should have a list of texts where the chat history is shown
    @Test
    public void shouldHaveChatHistory() {
        ListView chatHistoryView = (ListView) activity.findViewById(R.id.chat_history);
        assertNotNull(chatHistoryView);
    }

    // if a user sends a chat message, it should appear in the textbox
    @Test
    public void shouldDisplayNewMessages() {
        ListView chatHistoryView = (ListView) activity.findViewById(R.id.chat_history);
        assertNotNull(chatHistoryView);

        ChatAdapter chatAdapter = activity.getChatAdapterAdapter();

        for (int m=0; m < 100; m++) {
            // new message arrives
            String testMessage = "message" + m;
            chatAdapter.addNewMessage(testMessage);

            // update robolectric
            Robolectric.shadowOf(chatHistoryView).populateItems();

            // child history view should have at least 1 child now
            assertTrue(chatHistoryView.getChildCount() > 0);

            boolean isDisplayed = false;
            TextView messageView = null;
            String displayedMessage = null;
            // the ListView should now display the new message
            for (int i=0; i < chatHistoryView.getChildCount(); i++) {
                messageView = (TextView)chatHistoryView.getChildAt(i);
                assertNotNull(messageView);
                displayedMessage = messageView.getText().toString();
                if (testMessage.equals(displayedMessage)) {
                    isDisplayed = true;
                }
            }
            assertNotNull(messageView);
            assertNotNull(displayedMessage);
            assertTrue(isDisplayed);
        }
    }

    // when the user hits the send button, the typed message should appear in the textbox
    @Test
    public void shouldSendChatMessage() {
        String testMessage = "test message!";

        // Insert text into the message input field
        EditText messageInput = (EditText) activity.findViewById(R.id.messageInput);
        assertNotNull(messageInput);
        messageInput.setText(testMessage);

        // Click send button
        Button sendButton = (Button) activity.findViewById(R.id.button_send_search);
        sendButton.performClick();

        assertEquals(messageInput.getText().length(), 0);

        // Message should now be contained in the chatAdapter
        ChatAdapter chatAdapter = activity.getChatAdapterAdapter();
        assertTrue(chatAdapter.contains(testMessage));

        // Update view
        ListView chatHistoryView = (ListView) activity.findViewById(R.id.chat_history);
        assertNotNull(chatHistoryView);
        Robolectric.shadowOf(chatHistoryView).populateItems();

        // child history view should have at least 1 child now
        assertTrue(chatHistoryView.getChildCount() > 0);

        // Message should be visible
        boolean isDisplayed = false;
        TextView messageView = null;
        String displayedMessage = null;
        // the ListView should now display the new message
        for (int i=0; i < chatHistoryView.getChildCount(); i++) {
            messageView = (TextView)chatHistoryView.getChildAt(i);
            assertNotNull(messageView);
            displayedMessage = messageView.getText().toString();
            if (testMessage.equals(displayedMessage)) {
                isDisplayed = true;
            }
        }
        assertNotNull(messageView);
        assertNotNull(displayedMessage);
        assertTrue(isDisplayed);
    }

    @Test
    public void shouldIgnoreEmptyMessages() {

        EditText messageInput = (EditText) activity.findViewById(R.id.messageInput);
        Button sendButton = (Button) activity.findViewById(R.id.button_send_search);
        ListView chatHistoryView = (ListView) activity.findViewById(R.id.chat_history);

        String testMessage = null;
        // Insert text into the message input field
        messageInput.setText(testMessage);
        // Click send button
        sendButton.performClick();
        // Update view
        Robolectric.shadowOf(chatHistoryView).populateItems();
        // child history view should still be empty
        assertTrue(chatHistoryView.getChildCount() == 0);

        testMessage = "";
        // Insert text into the message input field
        messageInput.setText(testMessage);
        // Click send button
        sendButton.performClick();
        // Update view
        Robolectric.shadowOf(chatHistoryView).populateItems();
        // child history view should still be empty
        assertTrue(chatHistoryView.getChildCount() == 0);

    }

    // TODO:above each chat message, the device id should be displayed

    // TODO: if the user enters the input field, the onscreen keyboard should push the rest of the ui
    // to the top

    // TODO: if the user restarts the application, the chat history should be still there
    // and it should be scrolled down to the bottom so the last message is visible

    /**
     * TODO: If a GCM message containing a new chat message is received, update the activity
     */
    @Test
    public void shouldUpdateOnChat() {

    }
}
