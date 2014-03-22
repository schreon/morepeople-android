package morepeople.android.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import morepeople.android.app.interfaces.IDataCallback;
import morepeople.android.app.structures.SearchEntry;

/**
 * SearchAdapter extends BaseAdapter and provides functions for managing the searchEntryList.
 */
public class SearchAdapter extends BaseAdapter {

    private List<SearchEntry> searchEntryList;

    public IDataCallback getOnQueueSuccess() {
        return onQueueSuccess;
    }

    public void setOnQueueSuccess(IDataCallback onQueueSuccess) {
        this.onQueueSuccess = onQueueSuccess;
    }

    public IDataCallback getOnQueueError() {
        return onQueueError;
    }

    public void setOnQueueError(IDataCallback onQueueError) {
        this.onQueueError = onQueueError;
    }

    private IDataCallback onQueueSuccess = null;
    private IDataCallback onQueueError = null;


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
            notifyDataSetChanged();
        }
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

        if (view == null) {
            group = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.listitem_queue, null);
            creatorView = (TextView) group.findViewById(R.id.search_creator);
            descriptionView = (TextView) group.findViewById(R.id.search_term);

            final LinearLayout fGroup = group;
            group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    final SearchEntry entry = (SearchEntry) fGroup.getTag();
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//
//                    // set title
//                    alertDialogBuilder.setTitle("Beitreten");
//
//                    // set dialog message
//                    alertDialogBuilder
//                            .setMessage("Möchtest Du \"" + entry.MATCH_TAG + "\" beitreten?")
//                            .setCancelable(false)
//                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    // enqueue for the same match tag
//                                    coreApi.queue(entry.MATCH_TAG, onQueueSuccess, onQueueError);
//                                }
//                            })
//                            .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    // if this button is clicked, just close
//                                    // the dialog box and do nothing
//                                    dialog.cancel();
//                                }
//                            });
//
//                    // create alert dialog
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//
//                    // show it
//                    alertDialog.show();
                }
            });
        } else {
            group = (LinearLayout) view;
            creatorView = (TextView) group.findViewById(R.id.search_creator);
            descriptionView = (TextView) group.findViewById(R.id.search_term);
        }

        SearchEntry searchEntry = searchEntryList.get(i);
        creatorView.setText(searchEntry.USER_NAME);
        descriptionView.setText(searchEntry.MATCH_TAG);

        group.setTag(searchEntry);

        return group;
    }
}
