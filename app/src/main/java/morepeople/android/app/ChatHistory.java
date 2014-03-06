package morepeople.android.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for a chat history. It contains a list of messages and extends BaseAdapter, so it can be used together with ListViews.
 */
public class ChatHistory extends BaseAdapter {
    private List<String> messageList;

    /**
     * Constructor of the ChatHistory adapter.
     */
    public ChatHistory() {
        this.messageList = new ArrayList<String>();
    }

    /**
     * @return the number of messages in this chat history.
     */
    @Override
    public int getCount() {
        return messageList.size();
    }

    /**
     * @param i the index of the desired chat message.
     * @return the chat message at the specified index.
     */
    @Override
    public Object getItem(int i) {
        return messageList.get(i);
    }

    /**
     * @param i the index of the desired chat message.
     * @return the id, represented as long, of the chat message. In this case, it equals the index.
     */
    @Override
    public long getItemId(int i) {
        return i;
    }


    /**
     * @param i the index of the desired chat message.
     * @param convertView if this is not null, it will be reused, else a new TextView instance will be created.
     * @param viewGroup the calling ViewGroup. This may not be null.
     * @return the View of the desired chat message.
     */
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        TextView textView;
        if (convertView == null) {
            if (viewGroup == null) {
                throw new RuntimeException("viewGroup may not be null!");
            }
            Context context = viewGroup.getContext();
            if (context == null) {
                throw new RuntimeException("The context of viewGroup may not be null!");
            }
            textView = new TextView(context);
        } else {
            textView = (TextView)convertView;
        }
        textView.setText(messageList.get(i));
        return textView;
    }

    /**
     * @param message the new message which is to be added to the chat history. May not be null or empty!
     */
    public void addNewMessage(String message) {
        if (message == null ) {
            throw new RuntimeException("message may not be null!");
        }

        if (message.length() == 0 ) {
            throw new RuntimeException("message may not be empty!");
        }


        messageList.add(message);
        notifyDataSetChanged();
    }

    /**
     * @param message the exact message to be searched in the chat history.
     * @return returns true if the message could be found in the chat history.
     */
    public boolean contains(String message) {
        return messageList.contains(message);
    }
}
