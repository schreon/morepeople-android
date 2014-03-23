package morepeople.android.app.core;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.ICoreClient;
import morepeople.android.app.interfaces.ICoreReadablePreferences;
import morepeople.android.app.interfaces.ICoreWritablePreferences;
import morepeople.android.app.interfaces.IDataCallback;
import morepeople.android.app.interfaces.IErrorCallback;
import morepeople.android.app.structures.Coordinates;

/**
 * Created by schreon on 3/20/14.
 */
public class CoreApi implements ICoreApi {
    private static final String TAG = "morepeople.android.app.core.CoreApi";

    /**
     * @param client
     * @param preferences
     */
    public CoreApi(ICoreClient client,
                   ICoreWritablePreferences preferences,
                   IDataCallback onServerResponse) {
        this.client = client;
        this.preferences = preferences;
        this.onServerResponse = onServerResponse;
    }

    private ICoreClient client;
    private ICoreWritablePreferences preferences;
    private IDataCallback onServerResponse;

    /**
     * Decorate a data hashmap with user information fields
     */
    private Map<String, Object> decorateWithUserInfo(Map<String, Object> data, ICoreWritablePreferences preferences) {
        Coordinates coordinates = preferences.getLastKnownCoordinates();
        String userId = preferences.getUserId();
        String userName = preferences.getUserName();

        HashMap<String, Object> userLoc = new HashMap<String, Object>();
        if (coordinates != null) {
            userLoc.put(Constants.PROPERTY_LONGITUDE, coordinates.getLongitude());
            userLoc.put(Constants.PROPERTY_LATITUDE, coordinates.getLatitude());
        } else {
            userLoc.put(Constants.PROPERTY_LONGITUDE, 0);
            userLoc.put(Constants.PROPERTY_LATITUDE, 0);
        }
        data.put(Constants.PROPERTY_COORDINATES, userLoc);
        data.put(Constants.PROPERTY_USER_ID, userId);
        data.put(Constants.PROPERTY_USER_NAME, userName);
        return data;
    }

    @Override
    public void loadState(IErrorCallback onError) {
        Log.d(TAG, "loadState");
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        decorateWithUserInfo(arguments, preferences);
        // load state from server
        client.doPostRequest("/state", arguments, onServerResponse, onError);
    }

    @Override
    public void getLobby(IErrorCallback onError) {
        Log.d(TAG, "lobby");
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, preferences);
        payload.put(Constants.PROPERTY_MATCH_TAG, preferences.getMatchTag());
        client.doPostRequest("/queue", payload, onServerResponse, onError);
    }

    @Override
    public void search(Coordinates coordinates, long radius, String searchTerm, IErrorCallback onError) {
        Log.d(TAG, "search");
        HashMap<String, String> rewrite = new HashMap<String, String>();
        rewrite.put("LON", String.valueOf(coordinates.getLongitude()));
        rewrite.put("LAT", String.valueOf(coordinates.getLatitude()));
        rewrite.put("RAD", "1000");
        if (searchTerm != null) {
            rewrite.put("SEARCH", searchTerm);
        }
        client.doGetRequest("/queue", rewrite, onServerResponse, onError);
    }

    @Override
    public void queue(String searchTerm, IErrorCallback onError) {
        Log.d(TAG, "queue");
        preferences.setMatchTag(searchTerm);
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, preferences);
        payload.put(Constants.PROPERTY_MATCH_TAG, searchTerm);
        client.doPostRequest("/queue", payload, onServerResponse, onError);
    }

    @Override
    public void cancel(IErrorCallback onError) {
        Log.d(TAG, "cancel");
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, preferences);
        client.doPostRequest("/cancel", payload, onServerResponse, onError);
    }

    @Override
    public void accept(IErrorCallback onError) {
        Log.d(TAG, "accept");
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, preferences);
        client.doPostRequest("/accept", payload, onServerResponse, onError);
    }

    @Override
    public void finish(IErrorCallback onError) {
        Log.d(TAG, "finish");
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, preferences);
        client.doPostRequest("/finish", payload, onServerResponse, onError);
    }

    @Override
    public void evaluate(Map<String, Object> evaluation, IErrorCallback onError) {
        Log.d(TAG, "evaluate");
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put(Constants.PROPERTY_EVALUATION, evaluation);
        decorateWithUserInfo(payload, preferences);
        client.doPostRequest("/evaluate", payload, onServerResponse, onError);
    }

    @Override
    public void confirmCancel(IErrorCallback onError) {
        Log.d(TAG, "confirmCancel");
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, preferences);
        client.doPostRequest("/confirmcancel", payload, onServerResponse, onError);
    }

    @Override
    public ICoreReadablePreferences getPreferences() {
        return this.preferences;
    }

}
