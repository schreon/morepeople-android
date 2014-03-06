package morepeople.android.app;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
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

    public void addEntry(SearchEntry searchEntry) {
        searchEntryList.add(searchEntry);
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
        return null;
    }
}
