package morepeople.android.app;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import morepeople.android.app.structures.SearchEntry;

/**
 * SearchAdapter extends BaseAdapter and provides functions for managing the searchEntryList.
 */
public class SearchAdapter extends BaseAdapter {
    private static final String TAG = "morepeople.android.app.SearchAdapter";
    private List<SearchEntry> searchEntryList;

    /**
     * Constructor of SearchAdapter class.
     * Inits new searchEntryList.
     */
    public SearchAdapter() {
        searchEntryList = new ArrayList<SearchEntry>();

    }

    /**
     * Clears the searchEntryList without notifying and therefore reloading the complete data
     */
    public void emptySilent() {
        searchEntryList.clear();
    }

    /**
     * Adds all searchEntries to the list and notifies changed data
     *
     * @param searchEntries -> collection of searchEntries
     */
    public void addAll(Collection<SearchEntry> searchEntries) {
        // TODO: sort by distance to the user
        if (searchEntries != null) {
            searchEntryList.addAll(searchEntries);
        }
        notifyDataSetChanged();
    }

    /**
     * Adds one searchEntry to the list and notifies changed data
     *
     * @param searchEntry
     */
    public void add(SearchEntry searchEntry) {
        searchEntryList.add(searchEntry);
        notifyDataSetChanged();
    }

    /**
     * @return size of searchEntryList
     */
    @Override
    public int getCount() {
        return searchEntryList.size();
    }

    /**
     * Get item for specific position
     *
     * @param i -> position
     * @return searchEntryList item at position i
     */
    @Override
    public Object getItem(int i) {
        return searchEntryList.get(i);
    }

    /**
     * Get item USER_ID for specific position
     *
     * @param i -> position
     * @return item USER_ID
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    public View.OnClickListener getOnSearchEntryClickListener() {
        return onSearchEntryClickListener;
    }

    public void setOnSearchEntryClickListener(View.OnClickListener onSearchEntryClickListener) {
        this.onSearchEntryClickListener = onSearchEntryClickListener;
    }

    private View.OnClickListener onSearchEntryClickListener = null;


    private int getColorByDistance(double distance) {
        Log.d(TAG, "getColorByDistance, distance="+distance);
        // from green (0km) to red (10km)
        distance = distance / 10.0 * 120.0;
        float hue = Math.min(Math.max(0.0f, 120.0f - (float)distance), 120.0f);
        Log.d(TAG, "getColorByDistance, hue=" + distance);
        float[] hsv = new float[3];
        hsv[0] = hue;
        hsv[1] = 1.0f;
        hsv[2] = 0.7f;
        return Color.HSVToColor(hsv);
    }
    /**
     * Get the view of a single search entry.
     * <p/>
     * TODO: mark the own search entry!
     *
     * @param i         -> position
     * @param view
     * @param viewGroup
     * @return group
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        LinearLayout group;
        TextView creatorView;
        TextView descriptionView;
        TextView distanceView;

        if (view == null) {
            group = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.listitem_queue, null);
        } else {
            group = (LinearLayout) view;
        }

        if (onSearchEntryClickListener != null) {
            group.setOnClickListener(onSearchEntryClickListener);
        }

        creatorView = (TextView) group.findViewById(R.id.search_creator);
        descriptionView = (TextView) group.findViewById(R.id.search_term);
        distanceView = (TextView) group.findViewById(R.id.search_distance);

        SearchEntry searchEntry = searchEntryList.get(i);
        creatorView.setText(searchEntry.USER_NAME);
        descriptionView.setText(searchEntry.MATCH_TAG);
        String distanceString = String.format("%.1f", searchEntry.DISTANCE) + "km";
        distanceView.setText(distanceString);
        distanceView.setTextColor(getColorByDistance(searchEntry.DISTANCE));
        group.setTag(searchEntry);

        return group;
    }
}
