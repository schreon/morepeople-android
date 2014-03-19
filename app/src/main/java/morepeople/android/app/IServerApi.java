package morepeople.android.app;

import android.location.Location;

/**
 *
 */
public interface IServerApi {
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
    public void loadState(IDataCallback onSuccess, IDataCallback onError);

    /**
     * This should be polled only from active activities.
     * @return nearby searches, or 'queues', by other users.
     */
    public void searchEnvironment(Location location, IDataCallback onSuccess, IDataCallback onErrork);

    /**
     * Request: Change state to QUEUED or update the search.
     * @param matchTag the tag the user is searching for.
     * @return contains the new state of the user, determined by the server.
     */
    public void queue(String matchTag, IDataCallback onSuccess, IDataCallback onError);

    /**
     * Request a transition to the CANCELLED state.
     * @return contains the new state of the user, determined by the server.
     */
    public void cancel(IDataCallback onSuccess, IDataCallback onError);

    /**
     * Request a transition from the CANCELLED state to the OFFLINE state.
     * @return contains the new state of the user, determined by the server.
     */
    public void confirmCancel(IDataCallback onSuccess, IDataCallback onError);

    /**
     * Request a transition from the OPEN to the ACCEPTED state.
     * @return contains the new state of the user, determined by the server.
     */
    public void accept(IDataCallback onSuccess, IDataCallback onError);

    /**
     * Request a transition from the RUNNING to the EVALUATION state.
     * @return contains the new state of the user, determined by the server.
     */
    public void finish(IDataCallback onSuccess, IDataCallback onError);

    /**
     * Request a transition from the EVALUATION to the OFFLINE state.
     * @return contains the new state of the user, determined by the server.
     */
    public void evaluate(IDataCallback onSuccess, IDataCallback onErrork);


}
