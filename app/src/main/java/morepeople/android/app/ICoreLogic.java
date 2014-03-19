package morepeople.android.app;

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

    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_USER_NAME = "USER_NAME";
    public static final String KEY_LOC = "LOC";
    public static final String KEY_LONGITUDE = "LONGITUDE";
    public static final String KEY_LATITUDE = "LATITUDE";
    public static final String KEY_MATCH_ID = "MATCH_ID";
    public static final String KEY_MATCH_TAG = "MATCH_TAG";
    public static final String KEY_TIMESTAMP = "TIMESTAMP";
    public static final String KEY_RESULTS = "RESULTS";

    /**
     * Example:
     *
     * {
     *      'USER_ID' : 'nsdfkuaernvkdjfgheknrvkdfjh02123nbsdfv',
     *      'USER_NAME' : 'Torsten Test',
     *      'LOC' : {
     *          'LONGITUDE' : 123,
     *          'LATITUDE' : 456
     *      }
     * }
     * @return a Map containing information about the location, id and name of the user.
     */
    public Map<String, Object> getUserInfo();

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
     * @param onSuccess
     * @param onError
     */
    public void finish(IDataCallback onSuccess, IDataCallback onError);

    /**
     * Send the evaluation about the match to the server
     * @param evaluation
     * @param onSuccess
     * @param onError
     */
    public void evaluate(Map<String, Object> evaluation, IDataCallback onSuccess, IDataCallback onError);

    /**
     * Confirm the cancel state
     * @param onSuccess
     * @param onError
     */
    public void confirmCancel(IDataCallback onSuccess, IDataCallback onError);
}
