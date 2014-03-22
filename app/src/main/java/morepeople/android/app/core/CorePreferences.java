package morepeople.android.app.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.Coordinates;
import morepeople.android.app.interfaces.ICorePreferences;

/**
 * Stores preference values in the shared preferences of the given context.
 * Contract: access to the respective fields ONLY takes place through this class, because it
 * caches values! This means: only ony instance of this class per Context!
 */
public class CorePreferences implements ICorePreferences {
    private static final String TAG = "morepeople.android.app.CorePreferences";
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String userName;
    private String userId;
    private String hostName;
    private Coordinates lastKnownCoordinates;

    public CorePreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.PROPERTY_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userName = null;
        userId = null;
        hostName = null;
        lastKnownCoordinates = null;
    }

    @Override
    public String getUserName() {
        if (userName == null) {
            userName = sharedPreferences.getString(Constants.PROPERTY_USER_NAME, null);
            Log.d(TAG, "already contains username:" + userName);
        }
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
        editor.putString(Constants.PROPERTY_USER_NAME, userName);
        editor.commit();
    }

    @Override
    public String getUserId() {
        if (userId == null) {
            userId = sharedPreferences.getString(Constants.PROPERTY_USER_ID, null);
            Log.d(TAG, "already contains user id:" + userId);
        }
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
        editor.putString(Constants.PROPERTY_USER_ID, userId);
        editor.commit();
    }

    @Override
    public String getServerHostName() {
        if (hostName == null) {
            hostName = sharedPreferences.getString(Constants.PROPERTY_HOSTNAME, null);
            Log.d(TAG, "already contains hostname:" + userId);
        }
        return hostName;
    }

    @Override
    public void setServerHostName(String serverHostName) {
        this.hostName = serverHostName;
        editor.putString(Constants.PROPERTY_HOSTNAME, serverHostName);
        editor.commit();
    }

    @Override
    public Coordinates getLastKnownCoordinates() {
        if (lastKnownCoordinates == null) {
            long longitudeBits, latitudeBits;
            if (sharedPreferences.contains(Constants.PROPERTY_LONGITUDE)) {
                longitudeBits = sharedPreferences.getLong(Constants.PROPERTY_LONGITUDE, 0);
            } else {
                return null;
            }
            if (sharedPreferences.contains(Constants.PROPERTY_LATITUDE)) {
                latitudeBits = sharedPreferences.getLong(Constants.PROPERTY_LATITUDE, 0);
            } else {
                return null;
            }
            lastKnownCoordinates = new Coordinates(Double.longBitsToDouble(longitudeBits), Double.longBitsToDouble(latitudeBits));
            Log.d(TAG, "already contains hostname:" + userId);
        }
        return lastKnownCoordinates;
    }

    @Override
    public void setLastKnownCoordinates(Coordinates lastKnownCoordinates) {
        this.lastKnownCoordinates = lastKnownCoordinates;
        long longitudeBits = Double.doubleToLongBits(lastKnownCoordinates.getLongitude());
        long latitudeBits = Double.doubleToLongBits(lastKnownCoordinates.getLatitude());
        editor.putLong(Constants.PROPERTY_LONGITUDE, longitudeBits);
        editor.putLong(Constants.PROPERTY_LATITUDE, latitudeBits);
        editor.commit();
    }
}
