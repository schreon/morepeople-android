package morepeople.android.app.core;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by schreon on 3/22/14.
 */
public class CoreUserName {
    private static final String TAG = "morepeople.android.app.core.CoreUserName";

    public String readUserName(Context context) {
        String userName = null;

        Log.d(TAG, "user name not existent. trying to get it from profile.");

        //get user name from profile
        Uri uri = ContactsContract.Profile.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.Profile.DISPLAY_NAME, ContactsContract.Profile.DISPLAY_NAME_PRIMARY};

        ContentResolver contentResolver = context.getContentResolver();

        if (contentResolver == null) throw new RuntimeException("contentResolver is null");

        Cursor c = contentResolver.query(uri, projection, null, null, null);

        if (c == null) throw new RuntimeException(TAG + " - query result is null");

        String[] columnNames = c.getColumnNames();
        c.moveToFirst();
        try {
            for (int nameIndex = 0; nameIndex < columnNames.length; nameIndex++) {
                String columnName = columnNames[nameIndex];
                int columnIndex = c.getColumnIndex(columnName);
                String columnValue = c.getString(columnIndex);
                if (columnValue != null) {
                    userName = columnValue;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return userName;
    }
}
