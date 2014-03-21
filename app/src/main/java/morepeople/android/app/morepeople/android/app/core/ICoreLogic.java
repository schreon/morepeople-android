package morepeople.android.app.morepeople.android.app.core;

import android.location.Location;

import java.util.Map;

/**
 * Created by schreon on 3/19/14.
 */
public interface ICoreLogic {
    public static enum UserState {
        OFFLINE,
        QUEUED,
        OPEN,
        ACCEPTED,
        RUNNING,
        FINISHED,
        CANCELLED
    }

    public static final String SHARED_PREFS = "morepeople.android.app.SHARED_PREFS";
    public static final String PROPERTY_USER_ID = "USER_ID";
    public static final String PROPERTY_USER_NAME = "USER_NAME";
    public static final String PROPERTY_LOC = "LOC";
    public static final String PROPERTY_LONGITUDE = "LONGITUDE";
    public static final String PROPERTY_LATITUDE = "LATITUDE";
    public static final String PROPERTY_MATCH_ID = "MATCH_ID";
    public static final String PROPERTY_MATCH_TAG = "MATCH_TAG";
    public static final String PROPERTY_TIMESTAMP = "TIMESTAMP";
    public static final String PROPERTY_RESULTS = "RESULTS";
    public static final String PROPERTY_STATE = "STATE";
    public static final String PROPERTY_EVALUATION = "EVALUATION";

    public void getLobby(IDataCallback onSuccess, IDataCallback onError);

    public void setState(UserState newState);

    /**
     * Load the state from the persistence layer
     *
     * @param onError
     */
    public void load(IDataCallback onError);

    /**
     * Search the environment for other users with similar intents
     *
     * @param location
     * @param radius
     * @param searchTerm
     */
    public void search(Location location, long radius, String searchTerm, IDataCallback onSuccess, IDataCallback onError);

    /**
     * Enqueue the user for the given search term
     *
     * @param searchTerm
     * @param onSuccess
     * @param onError
     */
    public void queue(String searchTerm, IDataCallback onSuccess, IDataCallback onError);

    /**
     * Cancel the search or the lobby
     *
     * @param onSuccess
     * @param onError
     */
    public void cancel(IDataCallback onSuccess, IDataCallback onError);


    /**
     * Accept the match
     *
     * @param onSuccess
     * @param onError
     */
    public void accept(IDataCallback onSuccess, IDataCallback onError);

    /**
     * Finish a match
     *
     * @param onSuccess
     * @param onError
     */
    public void finish(IDataCallback onSuccess, IDataCallback onError);

    /**
     * Send the evaluation about the match to the server
     *
     * @param evaluation
     * @param onSuccess
     * @param onError
     */
    public void evaluate(Map<String, Object> evaluation, IDataCallback onSuccess, IDataCallback onError);

    /**
     * Confirm the cancel state
     *
     * @param onSuccess
     * @param onError
     */
    public void confirmCancel(IDataCallback onSuccess, IDataCallback onError);
}
