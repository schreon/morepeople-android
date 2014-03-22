package morepeople.android.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.IDataCallback;
import morepeople.android.app.structures.Participant;
import morepeople.android.app.structures.UserState;

/**
 * ParticipantsAdapter extends BaseAdapter and includes methods for the participant list
 */
public class ParticipantsAdapter extends BaseAdapter {
    private List<Participant> participantList;
    private ICoreApi coreLogic;
    private IDataCallback onAcceptSuccess;
    private IDataCallback onAcceptError;
    /**
     * Constructor of ParticipantsAdapter class
     */
    public ParticipantsAdapter(ICoreApi coreLogic) {
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

        RelativeLayout group;
        TextView nameView;
        TextView statusView;

        if (view == null) {
            group = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.listitem_confirm, null);
            nameView = (TextView) group.findViewById(R.id.user_name);
            statusView = (TextView) group.findViewById(R.id.user_status);
        } else {
            group = (RelativeLayout) view;
            nameView = (TextView) group.findViewById(R.id.user_name);
            statusView = (TextView) group.findViewById(R.id.user_status);
        }

        Participant participant = participantList.get(i);
        nameView.setText(participant.name);
        switch(UserState.valueOf(participant.status)) {
            case OPEN:
                statusView.setText("Überlegt noch ...");
                break;
            case ACCEPTED:
                statusView.setText("Bestätigt!");
                break;
        }

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
