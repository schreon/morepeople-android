package morepeople.android.app.morepeople.android.app.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by schreon on 3/20/14.
 */
public class CoreLogic implements ICoreLogic {
    private Context context;
    private ICoreRegistrar registrar;
    private ICoreClient client;
    private ICoreStateHandler stateHandler;
    private ICoreLocation coreLocation;
    private ICoreUserInfo coreUserInfo;


    public CoreLogic(Context context, UserState currentState) {
        this.context = context;
        registrar = new CoreRegistrar(context);
        client = new CoreClient(readHostName());
        stateHandler = new CoreStateHandler(context, currentState);
        coreLocation = new CoreLocation(context);
        coreUserInfo = new CoreUserInfo(registrar, coreLocation, context);
    }

    /**
     * Decorate a data hashmap with user information fields
     * @param data
     * @param userId
     * @param userName
     * @param loc
     * @return
     */
    private Map<String, Object> decorateWithUserInfo(Map<String, Object> data, String userId, String userName, Location loc) {
        HashMap<String, Object> userLoc = new HashMap<String, Object>();
        if (loc != null) {
            userLoc.put(ICoreLogic.KEY_LONGITUDE, loc.getLongitude());
            userLoc.put(ICoreLogic.KEY_LATITUDE, loc.getLatitude());
        } else {
            userLoc.put(ICoreLogic.KEY_LONGITUDE, 0);
            userLoc.put(ICoreLogic.KEY_LATITUDE, 0);
        }
        data.put(ICoreLogic.KEY_LOC, userLoc);
        data.put(ICoreLogic.KEY_USER_ID, userId);
        data.put(ICoreLogic.KEY_USER_NAME, userName);
        return data;
    }

    @Override
    public void load(final IDataCallback onError) {
        final IDataCallback onSuccess = new IDataCallback() {
            @Override
            public void run(Object data) {
                // call state state handler with loaded state
                String userState = ((Map<String, String>) data).get(KEY_STATE);
                stateHandler.transferToState(UserState.valueOf(userState));
            }
        };
        coreUserInfo.load(new IDataCallback() {
            @Override
            public void run(Object data) {
                HashMap<String, Object> arguments = new HashMap<String, Object>();
                decorateWithUserInfo(arguments, coreUserInfo.getUserId(), coreUserInfo.getUserName(), coreUserInfo.getUserLocation());
                // load state from server
                client.doPostRequest("/state", arguments, onSuccess, onError);
            }
        }, onError);
    }

    @Override
    public void search(Location location, long radius, String searchTerm, IDataCallback onSuccess, IDataCallback onError) {
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
}