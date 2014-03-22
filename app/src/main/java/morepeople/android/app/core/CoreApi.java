package morepeople.android.app.core;

import android.content.Context;
import android.location.Location;

import java.util.HashMap;
import java.util.Map;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.ICallback;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.ICoreClient;
import morepeople.android.app.interfaces.ICoreFactory;
import morepeople.android.app.interfaces.ICoreLocationManager;
import morepeople.android.app.interfaces.ICorePreferences;
import morepeople.android.app.interfaces.ICoreRegistrar;
import morepeople.android.app.interfaces.ICoreStateHandler;
import morepeople.android.app.interfaces.IDataCallback;
import morepeople.android.app.interfaces.IErrorCallback;
import morepeople.android.app.interfaces.UserState;

/**
 * Created by schreon on 3/20/14.
 */
public class CoreApi implements ICoreApi {
    private Context context;
    private ICoreRegistrar registrar;

    /**
     * Dependency Injection.
     *
     * @param context
     * @param registrar
     * @param client
     * @param stateHandler
     * @param coreLocation
     * @param coreUserInfo
     */
    public CoreApi(Context context,
                   ICoreRegistrar registrar,
                   ICoreClient client,
                   ICoreStateHandler stateHandler,
                   ICoreLocationManager coreLocation,
                   ICorePreferences coreUserInfo) {
        this.context = context;
        this.registrar = registrar;
        this.client = client;
        this.stateHandler = stateHandler;
        this.coreLocation = coreLocation;
        this.coreUserInfo = coreUserInfo;
    }

    private ICoreClient client;
    private ICoreStateHandler stateHandler;
    private ICoreLocationManager coreLocation;
    private ICorePreferences coreUserInfo;

    /**
     * Decorate a data hashmap with user information fields
     *
     * @param data
     * @param userId
     * @param userName
     * @param loc
     * @return
     */
    private Map<String, Object> decorateWithUserInfo(Map<String, Object> data, String userId, String userName, Location loc) {
        HashMap<String, Object> userLoc = new HashMap<String, Object>();
        if (loc != null) {
            userLoc.put(Constants.PROPERTY_LONGITUDE, loc.getLongitude());
            userLoc.put(Constants.PROPERTY_LATITUDE, loc.getLatitude());
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
    public void getLobby(ICallback onSuccess, IErrorCallback onError) {
        HashMap<String, String> rewrite = new HashMap<String, String>();
        rewrite.put("USER_ID", coreUserInfo.getUserId());
        client.doGetRequest("/lobby", rewrite, onSuccess, onError);
    }


    @Override
    public void search(Location location, long radius, String searchTerm, ICallback onSuccess, IErrorCallback onError) {
        HashMap<String, String> rewrite = new HashMap<String, String>();
        rewrite.put("LON", String.valueOf(location.getLongitude()));
        rewrite.put("LAT", String.valueOf(location.getLatitude()));
        rewrite.put("RAD", "1000");
        if (searchTerm != null) {
            rewrite.put("SEARCH", searchTerm);
        }
        client.doGetRequest("/queue", rewrite, onSuccess, onError);
    }

    @Override
    public void queue(String searchTerm, ICallback onSuccess, IErrorCallback onError) {
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, coreUserInfo.getUserId(), coreUserInfo.getUserName(), coreUserInfo.getUserLocation());
        payload.put(Constants.PROPERTY_MATCH_TAG, searchTerm);
        client.doPostRequest("/queue", payload, onSuccess, onError);
    }

    @Override
    public void cancel(ICallback onSuccess, IErrorCallback onError) {
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, coreUserInfo.getUserId(), coreUserInfo.getUserName(), coreUserInfo.getUserLocation());
        client.doPostRequest("/cancel", payload, onSuccess, onError);
    }

    @Override
    public void accept(ICallback onSuccess, IErrorCallback onError) {
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, coreUserInfo.getUserId(), coreUserInfo.getUserName(), coreUserInfo.getUserLocation());
        client.doPostRequest("/accept", payload, onSuccess, onError);
    }

    @Override
    public void finish(ICallback onSuccess, IErrorCallback onError) {
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, coreUserInfo.getUserId(), coreUserInfo.getUserName(), coreUserInfo.getUserLocation());
        client.doPostRequest("/finish", payload, onSuccess, onError);
    }

    @Override
    public void evaluate(Map<String, Object> evaluation, ICallback onSuccess, IErrorCallback onError) {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put(Constants.PROPERTY_EVALUATION, evaluation);
        decorateWithUserInfo(payload, coreUserInfo.getUserId(), coreUserInfo.getUserName(), coreUserInfo.getUserLocation());
        client.doPostRequest("/evaluate", payload, onSuccess, onError);
    }

    @Override
    public void confirmCancel(ICallback onSuccess, IErrorCallback onError) {
        Map<String, Object> payload = new HashMap<String, Object>();
        decorateWithUserInfo(payload, coreUserInfo.getUserId(), coreUserInfo.getUserName(), coreUserInfo.getUserLocation());
        client.doPostRequest("/confirmcancel", payload, onSuccess, onError);
    }


}
