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

    private String userName;
    private String userId;
    private String hostName;
    private String matchTag;
    private Coordinates lastKnownCoordinates;
    private UserState currentUserState;
    private List<Participant> participantList;
    private List<SearchEntry> searchEntryList;

    private final Type participantListType = new TypeToken<List<Participant>>() {
    }.getType();
    private final Type searchEntryListType = new TypeToken<List<SearchEntry>>() {
    }.getType();

    private Gson gson;

    public CoreWritablePreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.PROPERTY_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userName = null;
        userId = null;
        hostName = null;
        lastKnownCoordinates = null;
        currentUserState = null;
        participantList = null;
        searchEntryList = null;
        matchTag = null;
        gson = new Gson();
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
            Log.d(TAG, "already contains user USER_ID:" + userId);
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
            String serialized = sharedPreferences.getString(Constants.PROPERTY_COORDINATES, null);
            if (serialized == null) {
                return null;
            }
            lastKnownCoordinates = gson.fromJson(serialized, Coordinates.class);
        }
        return lastKnownCoordinates;
    }

    @Override
    public void setLastKnownCoordinates(Coordinates lastKnownCoordinates) {
        this.lastKnownCoordinates = lastKnownCoordinates;
        String serialized = gson.toJson(lastKnownCoordinates);
        editor.putString(Constants.PROPERTY_COORDINATES, serialized);
        editor.commit();
    }

    @Override
    public UserState getCurrentUserState() {
        if (currentUserState == null) {
            currentUserState = UserState.valueOf(sharedPreferences.getString(Constants.PROPERTY_STATE, null));
            Log.d(TAG, "current state:" + userId);
        }
        return currentUserState;
    }

    @Override
    public void setCurrentUserState(UserState currentUserState) {
        this.currentUserState = currentUserState;
        editor.putString(Constants.PROPERTY_STATE, currentUserState.toString());
        editor.commit();
    }

    @Override
    public List<Participant> getParticipantList() {
        if (participantList == null) {
            String serialized = sharedPreferences.getString(Constants.PROPERTY_PARTICIPANTS, null);
            if (serialized == null) {
                return null;
            }
            participantList = gson.fromJson(serialized, participantListType);
        }
        return participantList;
    }

    @Override
    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
        String serialized = gson.toJson(participantList);
        editor.putString(Constants.PROPERTY_PARTICIPANTS, serialized);
        editor.commit();
    }

    @Override
    public void setParticipantListFromMap(List<Map> participantListMap) {
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
    public List<SearchEntry> getSearchEntryList() {
        if (searchEntryList == null) {
            String serialized = sharedPreferences.getString(Constants.PROPERTY_SEARCHENTRIES, null);
            if (serialized == null) {
                return null;
            }
            searchEntryList = gson.fromJson(serialized, searchEntryListType);
        }
        return searchEntryList;
    }

    @Override
    public void setSearchEntryList(List<SearchEntry> searchEntryList) {
        this.searchEntryList = searchEntryList;
        String serialized = gson.toJson(searchEntryList);
        editor.putString(Constants.PROPERTY_SEARCHENTRIES, serialized);
        editor.commit();
    }

    @Override
    public void setSearchEntryListFromMap(List<Map> searchEntryListMap) {
        Log.d(TAG, "setSearchEntryListFromMap");
        List<SearchEntry> newSearchEntryList = new ArrayList<SearchEntry>();
        for (Map<String, Object> searchEntryMap : searchEntryListMap) {
            String USER_ID = (String) searchEntryMap.get(Constants.PROPERTY_USER_ID);
            String USER_NAME = (String) searchEntryMap.get(Constants.PROPERTY_USER_NAME);
            String MATCH_TAG = (String) searchEntryMap.get(Constants.PROPERTY_MATCH_TAG);
            newSearchEntryList.add(new SearchEntry(USER_ID, MATCH_TAG, USER_NAME));
        }
        this.setSearchEntryList(newSearchEntryList);
    }

    @Override
    public String getMatchTag() {
        if (matchTag == null) {
            matchTag = sharedPreferences.getString(Constants.PROPERTY_MATCH_TAG, null);
        }
        return matchTag;
    }

    @Override
    public void setMatchTag(String matchTag) {
        this.matchTag = matchTag;
        editor.putString(Constants.PROPERTY_MATCH_TAG, matchTag);
        editor.commit();
    }
}
