package morepeople.android.app.structures;

/**
 * SearchEntry class
 */
public class SearchEntry {

    public String USER_ID;
    public String MATCH_TAG;
    public String USER_NAME;
    public double DISTANCE;

    /**
     * Constructor of SearchEntry class
     *
     * @param USER_ID   -> USER_ID of searchEntry
     * @param MATCH_TAG -> MATCH_TAG of searchEntry
     * @param USER_NAME -> USER_NAME of searchEntry
     */
    public SearchEntry(String USER_ID, String MATCH_TAG, String USER_NAME, double DISTANCE) {
        this.USER_ID = USER_ID;
        this.MATCH_TAG = MATCH_TAG;
        this.USER_NAME = USER_NAME;
        this.DISTANCE = DISTANCE;
    }
}
