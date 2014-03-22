package morepeople.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Map;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.UserState;
import morepeople.android.app.morepeople.android.app.core.CoreAPI;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.IDataCallback;

/**
 * This activity is shown when the match the user joined previously has started. There is a chat
 * with all other participants and a button to start navigation.
 */
public class ChatActivity extends Activity {

    private ChatAdapter chatAdapterAdapter;
    private ICoreApi coreLogic;

    /**
     * @param savedInstanceState contains the previous state of the activity if it was existent before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("morepeople");
        setContentView(R.layout.activity_chat);
        UserState currentState = null;
        try {
            currentState = UserState.valueOf(getIntent().getExtras().getString(Constants.PROPERTY_STATE));
        } catch (Exception e) {
            Log.e("ConfirmationActivity", e.getMessage());
        }
        coreLogic = new CoreAPI(this, currentState);
        chatAdapterAdapter = new ChatAdapter();
        ListView listView = (ListView) findViewById(R.id.chat_history);
        listView.setAdapter(chatAdapterAdapter);
        listView.smoothScrollToPosition(chatAdapterAdapter.getCount() - 1);

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

        Button btn_finish = (Button) findViewById(R.id.button_end_match);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coreLogic.finish(new IDataCallback() {
                    @Override
                    public void run(Object rawData) {
                        Map<String, Object> data = (Map<String, Object>) rawData;
                        // set state
                        coreLogic.setState(UserState.valueOf((String) data.get(Constants.PROPERTY_STATE)));
                    }
                }, null);
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
