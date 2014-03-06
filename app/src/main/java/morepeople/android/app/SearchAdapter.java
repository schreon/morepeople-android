package morepeople.android.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by schreon on 3/6/14.
 */
public class SearchAdapter extends BaseAdapter {
    private List<SearchEntry> searchEntryList;

    public SearchAdapter() {
        searchEntryList = new ArrayList<SearchEntry>();
    }

    public void emptySilent() {
        searchEntryList.clear();
    }

    public void addAll(Collection<SearchEntry> searchEntries) {
        searchEntryList.addAll(searchEntries);
        notifyDataSetChanged();
    }
    public void add(SearchEntry searchEntry) {
        searchEntryList.add(searchEntry);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return searchEntryList.size();
    }

    @Override
    public Object getItem(int i) {
        return searchEntryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        LinearLayout group;
        TextView creatorView;
        TextView descriptionView;
        TextView participantsView;

        if ( view == null ) {
             group = new LinearLayout(context);
             creatorView = new TextView(context);
             descriptionView = new TextView(context);
             participantsView = new TextView(context);

            group.addView(descriptionView);
            group.addView(creatorView);
            group.addView(participantsView);
        } else {
            group = (LinearLayout) view;
            descriptionView = (TextView)group.getChildAt(0);
            creatorView = (TextView)group.getChildAt(1);
            participantsView = (TextView)group.getChildAt(2);
        }

        SearchEntry searchEntry = searchEntryList.get(i);
        creatorView.setText(searchEntry.creator);
        descriptionView.setText(searchEntry.description);
        participantsView.setText(searchEntry.participants);

        group.setTag(searchEntry);

        return group;
    }
}
