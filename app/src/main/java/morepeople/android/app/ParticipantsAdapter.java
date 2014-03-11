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
 * Created by schreon on 3/11/14.
 */
public class ParticipantsAdapter extends BaseAdapter {
    private List<Participant> participantList;

    public ParticipantsAdapter() {
        participantList = new ArrayList<Participant>();
    }
    @Override
    public int getCount() {
        return participantList.size();
    }

    @Override
    public Object getItem(int i) {
        return participantList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

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

    public void add(Participant participant) {
        participantList.add(participant);
        notifyDataSetChanged();
    }
}
