package morepeople.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChatActivity extends Activity {


    private ChatHistory chatHistoryAdapter;

    /**
     * @param savedInstanceState contains the previous state of the activity if it was existent before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatHistoryAdapter = new ChatHistory();
        ListView listView = (ListView) findViewById(R.id.chat_history);
        listView.setAdapter(chatHistoryAdapter);
        listView.smoothScrollToPosition(chatHistoryAdapter.getCount()-1);

        Button btn = (Button) findViewById(R.id.send_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageInput = (EditText) findViewById(R.id.messageInput);
                Object message = messageInput.getText();
                if (message == null) return;
                if (message.toString().equals("")) return;
                chatHistoryAdapter.addNewMessage(message.toString());
                // scroll
                ListView listView = (ListView) findViewById(R.id.chat_history);
                listView.smoothScrollToPosition(chatHistoryAdapter.getCount() - 1);
            }
        });
    }

    /**
     * @return the chatHistoryAdapter of this activity.
     */
    public ChatHistory getChatHistoryAdapter() {
        return chatHistoryAdapter;
    }

}
