package morepeople.android.app;

/**
 * Created by schreon on 3/6/14.
 */
public class SearchEntry {
    public String id;
    public String description;
    public String creator;
    public String participants;

    public SearchEntry(String id, String description, String creator, String participants) {
        this.id = id;
        this.description = description;
        this.creator = creator;
        this.participants = participants;
    }
}
