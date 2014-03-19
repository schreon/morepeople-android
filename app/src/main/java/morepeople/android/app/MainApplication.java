package morepeople.android.app;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * The core application.
 * <p/>
 * TODO:
 * use the broadcast system to request states
 * and
 */
public class MainApplication extends Application implements ICoreLogic {


    public static Runnable preInit = null;
    private HashMap<String, Object> userInfo = new HashMap<String, Object>();

    private void handleStateTransition(UserState userState) {
        Intent intent;
        Context context = this;
        switch (userState) {
            case OFFLINE:
                Log.d("UserState", "SearchActivity");
                intent = new Intent(context, SearchActivity.class);
                break;
            case QUEUED:
                Log.d("UserState", "launch SearchActivity");
                intent = new Intent(context, SearchActivity.class);
                break;
            case OPEN:
                Log.d("UserState", "launch ConfirmationActivity");
                intent = new Intent(context, ConfirmationActivity.class);
                break;
            case ACCEPTED:
                Log.d("UserState", "launch ConfirmationActivity");
                intent = new Intent(context, ConfirmationActivity.class);
                break;
            case RUNNING:
                Log.d("UserState", "launch ChatActivity");
                intent = new Intent(context, ChatActivity.class);
                break;
            case FINISHED:
                Log.d("UserState", "launch EvaluationActivity");
                intent = new Intent(context, EvaluationActivity.class);
                ;
                break;
            case CANCELLED:
                Log.d("UserState", "launch CancelActivity");
                intent = new Intent(context, CancelActivity.class);
                break;
            default:
                Log.d("UserState", "invalid serverstate");
                intent = new Intent(context, CancelActivity.class);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public Map<String, Object> getUserInfo() {
        return new HashMap<String, Object>(userInfo);
    }

    @Override
    public void search(Location location, long radius, String searchTerm, IDataCallback onSuccess, IDataCallback onError) {
        // TODO
    }

    @Override
    public void queue(String searchTerm, IDataCallback onSuccess, IDataCallback onError) {
        // TODO
    }

    @Override
    public void cancel(IDataCallback onSuccess, IDataCallback onError) {
        // TODO
    }

    @Override
    public void accept(IDataCallback onSuccess, IDataCallback onError) {
        // TODO
    }

    @Override
    public void finish(IDataCallback onSuccess, IDataCallback onError) {
        // TODO
    }

    @Override
    public void evaluate(Map<String, Object> evaluation, IDataCallback onSuccess, IDataCallback onError) {
        // TODO
    }

    @Override
    public void confirmCancel(IDataCallback onSuccess, IDataCallback onError) {
        // TODO
    }

    ;

    /**
     * Grab the user name.
     *
     * @return userName
     */
    private void readUserName() {
        String userName = null;

        //get username from shared preferences
        SharedPreferences sp1 = getSharedPreferences("MorePeople", Context.MODE_PRIVATE);
        if (sp1.contains("appUsername")) {
            userName = sp1.getString("appUsername", null);

            Log.d("ACCOUNT", "already contains username:" + userName);
            return;
        }

        Log.d("ACCOUNT", "user name not existent. trying to get it from profile.");

        //get user name from profile
        Uri uri = ContactsContract.Profile.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.Profile.DISPLAY_NAME, ContactsContract.Profile.DISPLAY_NAME_PRIMARY};

        ContentResolver contentResolver = getContentResolver();

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
            SharedPreferences sp = getSharedPreferences("MorePeople", Context.MODE_PRIVATE);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("appUsername", userName);
            Ed.commit();
            return;
        }

        Log.d("ACCOUNT", "could not determine username. returning null, expecting app will prompt for username later");
    }

    private Map<String, Object> convertLocation(Location loc) {
        HashMap<String, Object> locMap = new HashMap<String, Object>();
        if (loc != null) {
            locMap.put("LONGITUDE", loc.getLongitude());
            locMap.put("LATITUDE", loc.getLatitude());
        } else {
            locMap.put("LONGITUDE", 0);
            locMap.put("LATITUDE", 0);
        }
        return locMap;
    }

    private String getRegistrationId() {
        SharedPreferences prefs = getSharedPreferences("MorePeople", Context.MODE_PRIVATE);
        return prefs.getString(MainRegistrar.PROPERTY_REG_ID, "BAD_ID");
    }

    private void loadStateFromServer(Location location) {

        // Put everything into userInfo
        userInfo.put("USER_ID", getRegistrationId());
        SharedPreferences sp1 = this.getSharedPreferences("MorePeople", Context.MODE_PRIVATE);
        userInfo.put("USER_NAME", sp1.getString("appUsername", "no username defined"));
        userInfo.put("LOC", convertLocation(location));

        IDataCallback loadStateSuccess = new IDataCallback() {
            @Override
            public void run(Map<String, Object> data) {
                Log.d("MainApplication", "loadStateSuccess");
                handleStateTransition(UserState.valueOf((String) data.get("STATE")));
            }
        };

        final Context context = this;
        IDataCallback loadStateError = new IDataCallback() {
            @Override
            public void run(final Map<String, Object> data) {

                Log.d("MainApplication", "loadStateError");
                Handler mainHandler = new Handler(context.getMainLooper());

                Runnable runOnUI = new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Fehler: " + data.get("ERROR").toString(), Toast.LENGTH_LONG).show();
                    }
                }; // This is your code
                mainHandler.post(runOnUI);
            }
        };

        // Load state from server
        serverAPI.loadState(loadStateSuccess, loadStateError);
    }

    public void init() {
        instance = this;
        serverAPI = new ServerApi();

        // As soon as the reg id is there, read the username
        readUserName();

        MainRegistrar.requestRegistrationId(this, new Runnable() {
            @Override
            public void run() {


                LocationWrapper lw = new LocationWrapper();
                lw.requestLocation(instance, new LocationResponseHandler() {
                    @Override
                    public void gotInstantTemporaryLocation(Location location) {
                        // nothing
                    }

                    @Override
                    public void gotFallbackLocation(Location location) {
                        loadStateFromServer(location);
                    }

                    @Override
                    public void gotNewLocation(Location location) {
                        loadStateFromServer(location);
                    }
                }, 10000);

            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (preInit != null) {
            preInit.run();
        }

        init();
    }


}
