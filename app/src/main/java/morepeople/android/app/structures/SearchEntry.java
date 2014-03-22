package morepeople.android.app.structures;

/**
 * SearchEntry class
 */
public class SearchEntry {

    public String id;
    public String description;
    public String creator;

    /**
     * Constructor of SearchEntry class
     *
     * @param id           -> id of searchEntry
     * @param description  -> description of searchEntry
     * @param creator      -> creator of searchEntry
     */
    public SearchEntry(String id, String description, String creator) {
        this.id = id;
        this.description = description;
        this.creator = creator;
    }
}
