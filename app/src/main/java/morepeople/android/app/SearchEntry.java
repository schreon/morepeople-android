package morepeople.android.app;

/**
 * SearchEntry class
 */
public class SearchEntry {

    public String id;
    public String description;
    public String creator;
    public String participants;

    /**
     * Constructor of SearchEntry class
     * @param id -> id of searchEntry
     * @param description -> description of searchEntry
     * @param creator -> creator of searchEntry
     * @param participants -> number of participants
     */
    public SearchEntry(String id, String description, String creator, String participants) {
        this.id = id;
        this.description = description;
        this.creator = creator;
        this.participants = participants;
    }
}
