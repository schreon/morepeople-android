package morepeople.android.app.structures;

/**
 * Participant class
 */
public class Participant {

    public String USER_ID;
    public String USER_NAME;
    public String STATE;

    /**
     * Constructor of participant class
     *
     * @param USER_ID   -> USER_ID of the user
     * @param USER_NAME -> USER_NAME of the user
     * @param STATE     -> STATE of the user (OPEN, WAITING, RUNNING, FINISHED)
     */
    public Participant(String USER_ID, String USER_NAME, String STATE) {
        this.USER_ID = USER_ID;
        this.USER_NAME = USER_NAME;
        this.STATE = STATE;
    }
}
