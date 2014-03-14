package morepeople.android.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * The core application.
 *
 * TODO:
 * use the broadcast system to request states
 * and
 */
public class MainApplication extends Application {

    public static enum UserState {
        OFFLINE,
        QUEUED,
        OPEN,
        ACCEPTED,
        RUNNING,
        FINISHED,
        CANCELLED
    }

    private static MainApplication instance;
    private static IServerAPI serverAPI;
    private static HashMap<String, Object> userInfo = new HashMap<String, Object>();

    public static MainApplication getInstance() {
        return instance;
    }

    public static HashMap<String, Object> getUserInfo() {
        return new HashMap<String, Object>(userInfo);
    }

    /**
     * @return userName
     */
    private void readUserName() {
        String userName = null;

        //get username from shared preferences
        SharedPreferences sp1 = getSharedPreferences("MorePeople", 0);
        if (sp1.contains("appUsername")) {
            userName = sp1.getString("appUsername", null);

            Log.d("ACCOUNT", "already contains username:"+userName);
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
            Log.d("ACCOUNT", "found username in profile:"+userName);
            SharedPreferences sp = getSharedPreferences("MorePeople", 0);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("appUsername",userName);
            Ed.commit();
            return;
        }

        Log.d("ACCOUNT", "could not determine username. returning null, expecting app will prompt for username later");
    }

    /**
     * Get the user name, if null start EnterUserName activity.
     */
    private String getUserName() {
        SharedPreferences sp1 = this.getSharedPreferences("MorePeople", 0);
        return sp1.getString("appUsername", "no username defined");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        serverAPI = new ServerAPI();

        MainRegistrar.requestRegistrationId(this, new Runnable() {
            @Override
            public void run() {
                // As soon as the reg id is there, read the username
                readUserName();

                // Load state from server
                serverAPI.loadState(new DataCallback() {
                                @Override
                                public void run(Map<String, Object> data) {
                                    handleStateTransition(UserState.valueOf((String) data.get("STATE")));
                                }
                            }, new DataCallback() {
                                @Override
                                public void run(Map<String, Object> data) {
                                    Toast.makeText(instance, "Fehler: " + data.get("ERROR").toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                );
            }
        });
    }

    public static void handleStateTransition(UserState userState) {
        Intent intent;
        Context context = getInstance();
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
                intent = new Intent(context, EvaluationActivity.class);;
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
        getInstance().startActivity(intent);
    }


}
