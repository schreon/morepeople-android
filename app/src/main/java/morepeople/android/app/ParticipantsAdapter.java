package morepeople.android.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * ParticipantsAdapter extends BaseAdapter and includes methods for the participant list
 */
public class ParticipantsAdapter extends BaseAdapter {
    private List<Participant> participantList;

    /**
     * Constructor of ParticipantsAdapter class
     */
    public ParticipantsAdapter() {
        participantList = new ArrayList<Participant>();
    }

    /**
     * @return size of participantList
     */
    @Override
    public int getCount() {
        return participantList.size();
    }

    /**
     * Returns an item at specific position
     * @param i -> position
     * @return -> item at position i
     */
    @Override
    public Object getItem(int i) {
        return participantList.get(i);
    }

    /**
     * Gets item id at specific position
     * @param i -> position
     * @return -> id of item
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Gets the view
     * @param i -> position
     * @param view -> view
     * @param viewGroup -> viewgroup
     * @return group
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        assert context != null;

        LinearLayout group;
        TextView nameView;
        TextView statusView;

        if ( view == null ) {
            group = new LinearLayout(context);
            nameView = new TextView(context);
            statusView = new TextView(context);

            group.addView(nameView);
            group.addView(statusView);
        } else {
            group = (LinearLayout) view;
            nameView = (TextView)group.getChildAt(0);
            statusView = (TextView)group.getChildAt(1);
        }

        Participant participant = participantList.get(i);
        nameView.setText(participant.name);
        statusView.setText(participant.status);

        group.setTag(participant);

        return group;
    }

    /**
     * Adds an participant to the participantList
     * and notifies the data change
     * @param participant
     */
    public void add(Participant participant) {
        participantList.add(participant);
        notifyDataSetChanged();
    }
}
