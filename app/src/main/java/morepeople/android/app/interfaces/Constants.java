package morepeople.android.app.interfaces;

/**
 * Created by schreon on 3/22/14.
 */
public abstract class Constants {
    public static final String PROPERTY_USER_ID = "USER_ID";
    public static final String PROPERTY_USER_NAME = "USER_NAME";
    public static final String PROPERTY_COORDINATES = "LOC";
    public static final String PROPERTY_LONGITUDE = "lng";
    public static final String PROPERTY_LATITUDE = "lat";
    public static final String PROPERTY_MATCH_ID = "MATCH_ID";
    public static final String PROPERTY_MATCH_TAG = "MATCH_TAG";
    public static final String PROPERTY_TIMESTAMP = "TIMESTAMP";
    public static final String PROPERTY_RESULTS = "RESULTS";
    public static final String PROPERTY_STATE = "STATE";
    public static final String PROPERTY_EVALUATION = "EVALUATION";
    public static final String PROPERTY_SHARED_PREFS = "morepeople.android.app.PROPERTY_SHARED_PREFS";
    public static final String PROPERTY_HOSTNAME = "morepeople.android.app.HOSTNAME";
    public static final String PROPERTY_PARTICIPANTS = "PARTICIPANTS";
    public static final String PROPERTY_SEARCHENTRIES = "SEARCHENTRIES";
    public static final String PROPERTY_SERVERMESSAGE = "SERVERMESSAGE";
    public static final String PROPERTY_MESSAGE_TYPE = "MP_MESSAGE_TYPE";
    public static final String PROPERTY_DISTANCE = "DISTANCE";

    public static final String BROADCAST_GCM_MATCH_FOUND = "MATCH_FOUND";
    public static final String BROADCAST_GCM_CONFIRMATION = "CONFIRMATION";
    public static final String BROADCAST_GCM_RUNNING = "RUNNING";
    public static final String BROADCAST_GCM_CHAT = "CHAT";
    public static final String BROADCAST_LOCAL_MATCH_FOUND = "morepeople.android.app.BROADCAST_LOCAL_MATCH_FOUND";
    public static final String BROADCAST_LOCAL_CONFIRMATION = "morepeople.android.app.BROADCAST_LOCAL_CONFIRMATION";
    public static final String BROADCAST_LOCAL_RUNNING = "morepeople.android.app.BROADCAST_LOCAL_RUNNING";
    public static final String BROADCAST_LOCAL_CHAT = "morepeople.android.app.BROADCAST_LOCAL_CHAT";


    private Constants() {
        // Don't initialize this.
    }

}
