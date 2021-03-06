package morepeople.android.app.interfaces;

import java.util.Map;

import morepeople.android.app.structures.Coordinates;

/**
 * Contract: This is the only Core interface visible to activities along with ICoreWritablePreferences.
 */
public interface ICoreApi {

    /**
     * Load the state from the server.
     *
     * @param onError
     */
    public void loadState(IErrorCallback onError);

    /**
     * @param onError
     */
    public void getLobby(IErrorCallback onError);

    /**
     * Search the environment for other users with similar intents
     *
     * @param coordinates
     * @param radius
     * @param searchTerm
     */
    public void search(Coordinates coordinates, long radius, String searchTerm, IErrorCallback onError);

    /**
     * Enqueue the user for the given search term
     *
     * @param searchTerm
     * @param onError
     */
    public void queue(String searchTerm, IErrorCallback onError);

    /**
     * Cancel the search or the lobby
     *
     * @param onError
     */
    public void cancel(IErrorCallback onError);


    /**
     * Accept the match
     *
     * @param onError
     */
    public void accept(IErrorCallback onError);

    /**
     * Finish a match
     *
     * @param onError
     */
    public void finish(IErrorCallback onError);

    /**
     * Send the evaluation about the match to the server
     *
     * @param evaluation
     * @param onError
     */
    public void evaluate(Map<String, Object> evaluation, IErrorCallback onError);

    /**
     * Confirm the cancel state
     *
     * @param onError
     */
    public void confirmCancel(IErrorCallback onError);

    /**
     * @return read only preferences without any setters.
     */
    public ICoreReadablePreferences getPreferences();
}
