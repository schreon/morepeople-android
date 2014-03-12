package morepeople.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * This activity is shown when the match the user joined previously has started. There is a chat
 * with all other participants and a button to start navigation.
 */
public class ChatActivity extends Activity {

    private ChatAdapter chatAdapterAdapter;

    /**
     * @param savedInstanceState contains the previous state of the activity if it was existent before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatAdapterAdapter = new ChatAdapter();
        ListView listView = (ListView) findViewById(R.id.chat_history);
        listView.setAdapter(chatAdapterAdapter);
        listView.smoothScrollToPosition(chatAdapterAdapter.getCount()-1);

        Button btn = (Button) findViewById(R.id.button_send_search);
        /**
         * OnClickListener for send search button.
         * Adds search entry to the list.
         */
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageInput = (EditText) findViewById(R.id.messageInput);
                Object message = messageInput.getText();
                if (message == null) return;
                if (message.toString().equals("")) return;
                chatAdapterAdapter.addNewMessage(message.toString());
                messageInput.setText("");
                // scroll
                ListView listView = (ListView) findViewById(R.id.chat_history);
                listView.smoothScrollToPosition(chatAdapterAdapter.getCount() - 1);
            }
        });
    }

    /**
     * @return the chatAdapterAdapter of this activity.
     */
    public ChatAdapter getChatAdapterAdapter() {
        return chatAdapterAdapter;
    }

}
