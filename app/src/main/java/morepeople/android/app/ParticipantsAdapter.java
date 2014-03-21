package morepeople.android.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import morepeople.android.app.morepeople.android.app.core.CoreLogic;
import morepeople.android.app.morepeople.android.app.core.ICoreLogic;
import morepeople.android.app.morepeople.android.app.core.IDataCallback;

/**
 * ParticipantsAdapter extends BaseAdapter and includes methods for the participant list
 */
public class ParticipantsAdapter extends BaseAdapter {
    private List<Participant> participantList;
    private ICoreLogic coreLogic;
    private IDataCallback onAcceptSuccess;
    private IDataCallback onAcceptError;
    /**
     * Constructor of ParticipantsAdapter class
     */
    public ParticipantsAdapter(ICoreLogic coreLogic) {
        participantList = new ArrayList<Participant>();
        this.coreLogic = coreLogic;
    }

    /**
     * Clears the participantList without notifying and therefore reloading the complete data
     */
    public void emptySilent() {
        participantList.clear();
    }

    /**
     * Adds all participants to the list and notifies changed data
     *
     * @param participants -> collection of participants
     */
    public void addAll(Collection<Participant> participants) {
        participantList.addAll(participants);
        notifyDataSetChanged();

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
     *
     * @param i -> position
     * @return -> item at position i
     */
    @Override
    public Object getItem(int i) {
        return participantList.get(i);
    }

    /**
     * Gets item id at specific position
     *
     * @param i -> position
     * @return -> id of item
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Gets the view
     *
     * @param i         -> position
     * @param view      -> view
     * @param viewGroup -> viewgroup
     * @return group
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        assert context != null;

        LinearLayout group;
        TextView nameView;
        TextView statusView;

        if (view == null) {
            group = new LinearLayout(context);
            nameView = new TextView(context);
            statusView = new TextView(context);

            group.addView(nameView);
            group.addView(statusView);
        } else {
            group = (LinearLayout) view;
            nameView = (TextView) group.getChildAt(0);
            statusView = (TextView) group.getChildAt(1);
        }

        Participant participant = participantList.get(i);
        nameView.setText(participant.name);
        statusView.setText(participant.status);

        group.setTag(participant);

        // TODO: show profile info when clicking on participant

        return group;
    }

    /**
     * Adds an participant to the participantList
     * and notifies the data change
     *
     * @param participant
     */
    public void add(Participant participant) {
        participantList.add(participant);
        notifyDataSetChanged();
    }
}
