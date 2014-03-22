package morepeople.android.app.core;

import android.content.Context;
import android.location.Location;

import java.util.HashMap;
import java.util.Map;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.ICallback;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.ICoreClient;
import morepeople.android.app.interfaces.ICoreLocationManager;
import morepeople.android.app.interfaces.ICorePreferences;
import morepeople.android.app.interfaces.ICoreRegistrar;
import morepeople.android.app.interfaces.ICoreStateHandler;
import morepeople.android.app.interfaces.IDataCallback;
import morepeople.android.app.interfaces.IErrorCallback;
import morepeople.android.app.structures.Coordinates;

/**
 * Created by schreon on 3/20/14.
 */
public class CoreApi implements ICoreApi {
    private Context context;
    private ICoreRegistrar registrar;

    private IDataCallback onServerResponse;

    /**
     *
     *
     * @param context
     * @param registrar
     * @param client
     * @param stateHandler
     * @param coreLocation
     * @param preferences
     */
    public CoreApi(Context context,
                   ICoreRegistrar registrar,
                   ICoreClient client,
                   ICoreStateHandler stateHandler,
                   ICoreLocationManager coreLocation,
                   ICorePreferences preferences,
                   IDataCallback onServerResponse) {
        this.context = context;
        this.registrar = registrar;
        this.client = client;
        this.stateHandler = stateHandler;
        this.coreLocation = coreLocation;
        this.preferences = preferences;
        this.onServerResponse = onServerResponse;
    }

    private ICoreClient client;
    private ICoreStateHandler stateHandler;
    private ICoreLocationManager coreLocation;
    private ICorePreferences preferences;

    /**
     * Decorate a data hashmap with user information fields
     */
    private Map<String, Object> decorateWithUserInfo(Map<String, Object> data, ICorePreferences preferences) {
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
    public void getLobby(IErrorCallback onError) {
        HashMap<String, String> rewrite = new HashMap<String, String>();
        rewrite.put("USER_ID", preferences.getUserId());
        client.doGetRequest(
                "/lobby",
                rewrite,
                new IDataCallback() {
                    @Override
                    public void run(Map<String, Object> data) {

                    }
                },
                onError);
    }

    @Override
    public void search(Coordinates coordinates, long radius, String searchTerm, IErrorCallback onError) {
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
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, preferences);
        payload.put(Constants.PROPERTY_MATCH_TAG, searchTerm);
        client.doPostRequest("/queue", payload, onServerResponse, onError);
    }

    @Override
    public void cancel(IErrorCallback onError) {
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, preferences);
        client.doPostRequest("/cancel", payload, onServerResponse, onError);
    }

    @Override
    public void accept(IErrorCallback onError) {
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, preferences);
        client.doPostRequest("/accept", payload, onServerResponse, onError);
    }

    @Override
    public void finish(IErrorCallback onError) {
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, preferences);
        client.doPostRequest("/finish", payload, onServerResponse, onError);
    }

    @Override
    public void evaluate(Map<String, Object> evaluation, IErrorCallback onError) {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put(Constants.PROPERTY_EVALUATION, evaluation);
        decorateWithUserInfo(payload, preferences);
        client.doPostRequest("/evaluate", payload, onServerResponse, onError);
    }

    @Override
    public void confirmCancel(IErrorCallback onError) {
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, preferences);
        client.doPostRequest("/confirmcancel", payload, onServerResponse, onError);
    }

}
