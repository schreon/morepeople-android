package morepeople.android.app;

/**
 * Participant class
 */
public class Participant {

    public String id;
    public String name;
    public String status;

    /**
     * Constructor of participant class
     *
     * @param id     -> id of the user
     * @param name   -> name of the user
     * @param status -> status of the user (OPEN, WAITING, RUNNING, FINISHED)
     */
    public Participant(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }
}
