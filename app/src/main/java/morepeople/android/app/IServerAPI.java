package morepeople.android.app;

import java.util.Map;

/**
 * Should be called from within async tasks
 */
public interface IServerAPI {
    public static final String MATCH_ID = "MATCH_ID";
    public static final String USER_ID = "USER_ID";
    public static final String MATCH_TAG = "MATCH_TAG";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String RESULTS = "RESULTS";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String USER_NAME = "USER_NAME";

    /**
     * This should be run after errors occured or the app has been restarted.
     * @return the current state of the User
     */
    public Map<String, Object> getState();

    /**
     * This should be polled only from active activities.
     * @return nearby searches, or 'queues', by other users.
     */
    public Map<String, Object> searchEnvironment();

    /**
     * Request: Change state to QUEUED or update the search.
     * @param matchTag the tag the user is searching for.
     * @return contains the new state of the user, determined by the server.
     */
    public Map<String, Object> queue(String matchTag);

    /**
     * Request a transition to the CANCELLED state.
     * @return contains the new state of the user, determined by the server.
     */
    public Map<String, Object> cancel();

    /**
     * Request a transition from the CANCELLED state to the OFFLINE state.
     * @return contains the new state of the user, determined by the server.
     */
    public Map<String, Object> cancelconfirm();

    /**
     * Request a transition from the OPEN to the ACCEPTED state.
     * @return contains the new state of the user, determined by the server.
     */
    public Map<String, Object> accept();

    /**
     * Request a transition from the RUNNING to the EVALUATION state.
     * @return contains the new state of the user, determined by the server.
     */
    public Map<String,Object> finish();

    /**
     * Request a transition from the EVALUATION to the OFFLINE state.
     * @return contains the new state of the user, determined by the server.
     */
    public Map<String,Object> evaluate();


}
