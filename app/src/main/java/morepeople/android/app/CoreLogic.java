package morepeople.android.app;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by schreon on 3/20/14.
 */
public class CoreLogic implements ICoreLogic {
    private Context context;
    private CoreClient client;
    private Map<String, Object> userInfo;

    public CoreLogic(Context context) {
        this.context = context;
        client = new CoreClient(readHostName());
        readUserName();
        userInfo = new HashMap<String, Object>();
    }

    @Override
    public void load(IDataCallback onSuccess, IDataCallback onError) {

        // TODO: read user info if necessary and store it in shared prefs
        // TODO: load state from server

    }

    @Override
    public Map<String, Object> getUserInfo() {
        return null;
    }

    @Override
    public void search(Location location, long radius, String searchTerm, IDataCallback onSuccess, IDataCallback onError) {

    }

    @Override
    public void queue(String searchTerm, IDataCallback onSuccess, IDataCallback onError) {

    }

    @Override
    public void cancel(IDataCallback onSuccess, IDataCallback onError) {

    }

    @Override
    public void accept(IDataCallback onSuccess, IDataCallback onError) {

    }

    @Override
    public void finish(IDataCallback onSuccess, IDataCallback onError) {

    }

    @Override
    public void evaluate(Map<String, Object> evaluation, IDataCallback onSuccess, IDataCallback onError) {

    }

    @Override
    public void confirmCancel(IDataCallback onSuccess, IDataCallback onError) {

    }

    private String readHostName() {
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) ai.metaData.get("morepeople.android.app.HOSTNAME");
    }

    /**
     * Grab the user name.
     *
     * @return userName
     */
    private String readUserName() {
        String userName = null;

        //get username from shared preferences
        SharedPreferences sp1 = context.getSharedPreferences("MorePeople", Context.MODE_PRIVATE);
        if (sp1.contains("appUsername")) {
            userName = sp1.getString("appUsername", null);

            Log.d("ACCOUNT", "already contains username:" + userName);
            return userName;
        }

        Log.d("ACCOUNT", "user name not existent. trying to get it from profile.");

        //get user name from profile
        Uri uri = ContactsContract.Profile.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.Profile.DISPLAY_NAME, ContactsContract.Profile.DISPLAY_NAME_PRIMARY};

        ContentResolver contentResolver = context.getContentResolver();

        if (contentResolver == null) throw new RuntimeException("contentResolver is null");

        Cursor c = contentResolver.query(uri, projection, null, null, null);

        if (c == null) throw new RuntimeException("query result is null");

        String[] columnNames = c.getColumnNames();
        c.moveToFirst();
        try {
            for (int nameIndex = 0; nameIndex < columnNames.length; nameIndex++) {
                Log.d("ACCOUNT nameIndex", Integer.toString(nameIndex));
                String columnName = columnNames[nameIndex];
                Log.d("ACCOUNT columnName", columnName);
                int columnIndex = c.getColumnIndex(columnName);
                Log.d("ACCOUNT columnIndex", Integer.toString(columnIndex));
                String columnValue = c.getString(columnIndex);
                Log.d("ACCOUNT columnValue", columnValue);
                if (columnValue != null) {
                    userName = columnValue;
                }
            }
        } catch (Exception e) {
            Log.e("ACCOUNT", e.toString());
        }

        //save username in prefs
        if (userName != null) {
            Log.d("ACCOUNT", "found username in profile:" + userName);
            SharedPreferences sp = context.getSharedPreferences("MorePeople", Context.MODE_PRIVATE);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("appUsername", userName);
            Ed.commit();
            return userName;
        }

        Log.d("ACCOUNT", "could not determine username. returning null, expecting app will prompt for username later");
        return null;
    }
}
