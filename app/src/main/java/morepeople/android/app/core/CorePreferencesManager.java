package morepeople.android.app.core;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import morepeople.android.app.interfaces.ICoreAPI;
import morepeople.android.app.interfaces.ICoreLocationManager;
import morepeople.android.app.interfaces.ICorePreferencesManager;
import morepeople.android.app.interfaces.ICoreRegistrar;
import morepeople.android.app.interfaces.IDataCallback;

/**
 * Created by schreon on 3/20/14.
 */
public class CorePreferencesManager implements ICorePreferencesManager {

    private boolean finishedLocation;
    private boolean finishedRegistration;

    private ICoreRegistrar registrar;
    private ICoreLocationManager location;

    private Location userLocation;
    private String regId;
    private String userName;

    private Context context;

    public CorePreferencesManager(ICoreRegistrar registrar, ICoreLocationManager location, Context context) {
        this.registrar = registrar;
        this.location = location;

        finishedLocation = false;
        finishedRegistration = false;
        this.context = context;

        // set last known location
        this.userLocation = location.getLastKnownLocation();

        // get reg id
        this.regId = registrar.getRegistrationId();

        // get userName
        this.userName = readUserName();
    }

    /**
     * Grab the user name.
     *
     * @return userName
     */
    private String readUserName() {
        String userName = null;

        //get username from shared preferences
        SharedPreferences sp1 = context.getSharedPreferences(ICoreAPI.SHARED_PREFS, Context.MODE_PRIVATE);
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
            SharedPreferences sp = context.getSharedPreferences(ICoreAPI.SHARED_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("appUsername", userName);
            Ed.commit();
            return userName;
        }

        Log.d("ACCOUNT", "could not determine username. returning null, expecting app will prompt for username later");
        return null;
    }

    private synchronized void checkFinishedIdAndLoc(final IDataCallback onSuccess) {
        if (finishedLocation && finishedRegistration) {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put(ICoreAPI.PROPERTY_LOC, userLocation);
            data.put(ICoreAPI.PROPERTY_USER_ID, regId);
            data.put(ICoreAPI.PROPERTY_USER_NAME, userName);
            onSuccess.run(data);
        }
    }

    @Override
    public void initialize(final ICallback onSuccess, final IDataCallback onError) {
        // read the username
        userName = readUserName();

        // request location
        Location loc = location.getLastKnownLocation();
        if (loc != null) {
            finishedLocation = true;
        } else {
            location.setLocationUpdateHandler(new IDataCallback() {
                @Override
                public void run(Object data) {
                    location.setListenToLocationUpdates(false);
                    finishedLocation = true;
                    userLocation = (Location) data;
                    checkFinishedIdAndLoc(onSuccess);
                }
            });
            location.setListenToLocationUpdates(true);
        }

        // request regId
        registrar.register(new IDataCallback() {
            @Override
            public void run(Object data) {
                regId = (String) data;
                finishedRegistration = true;
                checkFinishedIdAndLoc(onSuccess);
            }
        }, onError);
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getUserId() {
        return regId;
    }

    @Override
    public Location getUserLocation() {
        return userLocation;
    }
}
