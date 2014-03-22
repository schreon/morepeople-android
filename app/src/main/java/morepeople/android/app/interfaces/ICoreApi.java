package morepeople.android.app.interfaces;

import android.location.Location;

import java.util.Map;

/**
 * Created by schreon on 3/19/14.
 */
public interface ICoreApi {

    /**
     *
     * @param onSuccess
     * @param onError
     */
    public void getLobby(ICallback onSuccess, IErrorCallback onError);

    /**
     * Initialize the API.
     *
     * @param onError
     */
    public void initialize(ICallback onFinished, IErrorCallback onError);

    /**
     * Search the environment for other users with similar intents
     *
     * @param location
     * @param radius
     * @param searchTerm
     */
    public void search(Location location, long radius, String searchTerm, ICallback onSuccess, IErrorCallback onError);

    /**
     * Enqueue the user for the given search term
     *
     * @param searchTerm
     * @param onSuccess
     * @param onError
     */
    public void queue(String searchTerm, ICallback onSuccess, IErrorCallback onError);

    /**
     * Cancel the search or the lobby
     *
     * @param onSuccess
     * @param onError
     */
    public void cancel(ICallback onSuccess, IErrorCallback onError);


    /**
     * Accept the match
     *
     * @param onSuccess
     * @param onError
     */
    public void accept(ICallback onSuccess, IErrorCallback onError);

    /**
     * Finish a match
     *
     * @param onSuccess
     * @param onError
     */
    public void finish(ICallback onSuccess, IErrorCallback onError);

    /**
     * Send the evaluation about the match to the server
     *
     * @param evaluation
     * @param onSuccess
     * @param onError
     */
    public void evaluate(Map<String, Object> evaluation, ICallback onSuccess, IErrorCallback onError);

    /**
     * Confirm the cancel state
     *
     * @param onSuccess
     * @param onError
     */
    public void confirmCancel(ICallback onSuccess, IErrorCallback onError);
}
