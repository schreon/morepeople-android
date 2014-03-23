package morepeople.android.app.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.ICoreWritablePreferences;
import morepeople.android.app.structures.Coordinates;
import morepeople.android.app.structures.Participant;
import morepeople.android.app.structures.SearchEntry;
import morepeople.android.app.structures.UserState;

/**
 * Stores preference values in the shared preferences of the given context.
 * Contract: access to the respective fields ONLY takes place through this class, because it
 * caches values! This means: only ony instance of this class per Context!
 */
public class CoreWritablePreferences implements ICoreWritablePreferences {
    private static final String TAG = "morepeople.android.app.CoreWritablePreferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private final Type participantListType = new TypeToken<List<Participant>>() {
    }.getType();
    private final Type searchEntryListType = new TypeToken<List<SearchEntry>>() {
    }.getType();

    private Gson gson;

    public CoreWritablePreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.PROPERTY_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    @Override
    public synchronized String getUserName() {
        String userName;
        userName = sharedPreferences.getString(Constants.PROPERTY_USER_NAME, null);
        Log.d(TAG, "already contains username:" + userName);
        return userName;
    }

    @Override
    public synchronized void setUserName(String userName) {
        editor.putString(Constants.PROPERTY_USER_NAME, userName);
        editor.commit();
    }

    @Override
    public synchronized String getUserId() {
        String userId = sharedPreferences.getString(Constants.PROPERTY_USER_ID, null);
        return userId;
    }

    @Override
    public synchronized void setUserId(String userId) {
        editor.putString(Constants.PROPERTY_USER_ID, userId);
        editor.commit();
    }

    @Override
    public synchronized String getServerHostName() {
        String hostName = sharedPreferences.getString(Constants.PROPERTY_HOSTNAME, null);
        return hostName;
    }

    @Override
    public synchronized void setServerHostName(String serverHostName) {
        editor.putString(Constants.PROPERTY_HOSTNAME, serverHostName);
        editor.commit();
    }

    @Override
    public synchronized Coordinates getLastKnownCoordinates() {
        String serialized = sharedPreferences.getString(Constants.PROPERTY_COORDINATES, null);
        if (serialized == null) {
            return null;
        }
        return gson.fromJson(serialized, Coordinates.class);
    }

    @Override
    public void setLastKnownCoordinates(Coordinates lastKnownCoordinates) {
        String serialized = gson.toJson(lastKnownCoordinates);
        editor.putString(Constants.PROPERTY_COORDINATES, serialized);
        editor.commit();
    }

    @Override
    public synchronized UserState getCurrentUserState() {
        UserState currentUserState = UserState.valueOf(sharedPreferences.getString(Constants.PROPERTY_STATE, null));
        return currentUserState;
    }

    @Override
    public synchronized void setCurrentUserState(UserState currentUserState) {
        editor.putString(Constants.PROPERTY_STATE, currentUserState.toString());
        editor.commit();
    }

    @Override
    public synchronized List<Participant> getParticipantList() {
        String serialized = sharedPreferences.getString(Constants.PROPERTY_PARTICIPANTS, null);
        if (serialized == null) {
            return null;
        }
        List<Participant> participantList = gson.fromJson(serialized, participantListType);
        return participantList;
    }

    @Override
    public synchronized void setParticipantList(List<Participant> participantList) {
        String serialized = gson.toJson(participantList);
        editor.putString(Constants.PROPERTY_PARTICIPANTS, serialized);
        editor.commit();
    }

    @Override
    public synchronized void setParticipantListFromMap(List<Map> participantListMap) {
        Log.d(TAG, "setParticipantListFromMap");
        List<Participant> newParticipantList = new ArrayList<Participant>();
        for (Map<String, Object> participantMap : participantListMap) {
            String USER_ID = (String) participantMap.get(Constants.PROPERTY_USER_ID);
            String USER_NAME = (String) participantMap.get(Constants.PROPERTY_USER_NAME);
            String STATE = (String) participantMap.get(Constants.PROPERTY_STATE);
            newParticipantList.add(new Participant(USER_ID, USER_NAME, STATE));
        }
        this.setParticipantList(newParticipantList);
    }


    @Override
    public synchronized List<SearchEntry> getSearchEntryList() {
        String serialized = sharedPreferences.getString(Constants.PROPERTY_SEARCHENTRIES, null);
        if (serialized == null) {
            return null;
        }
        List<SearchEntry> searchEntryList = gson.fromJson(serialized, searchEntryListType);
        return searchEntryList;
    }

    @Override
    public synchronized void setSearchEntryList(List<SearchEntry> searchEntryList) {
        String serialized = gson.toJson(searchEntryList);
        editor.putString(Constants.PROPERTY_SEARCHENTRIES, serialized);
        editor.commit();
    }

    @Override
    public synchronized void setSearchEntryListFromMap(List<Map> searchEntryListMap) {
        Log.d(TAG, "setSearchEntryListFromMap");
        List<SearchEntry> newSearchEntryList = new ArrayList<SearchEntry>();
        for (Map<String, Object> searchEntryMap : searchEntryListMap) {
            String USER_ID = (String) searchEntryMap.get(Constants.PROPERTY_USER_ID);
            String USER_NAME = (String) searchEntryMap.get(Constants.PROPERTY_USER_NAME);
            String MATCH_TAG = (String) searchEntryMap.get(Constants.PROPERTY_MATCH_TAG);
            double DISTANCE;
            if (searchEntryMap.containsKey(Constants.PROPERTY_DISTANCE)) {
                DISTANCE = (Double) searchEntryMap.get(Constants.PROPERTY_DISTANCE);
            } else {
                DISTANCE = 0.0;
            }

            newSearchEntryList.add(new SearchEntry(USER_ID, MATCH_TAG, USER_NAME, DISTANCE));
        }
        this.setSearchEntryList(newSearchEntryList);
    }

    @Override
    public synchronized String getMatchTag() {
        String matchTag = sharedPreferences.getString(Constants.PROPERTY_MATCH_TAG, null);
        return matchTag;
    }

    @Override
    public synchronized void setMatchTag(String matchTag) {
        editor.putString(Constants.PROPERTY_MATCH_TAG, matchTag);
        editor.commit();
    }
}
