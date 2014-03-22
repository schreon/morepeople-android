package morepeople.android.app.interfaces;

import java.util.List;

import morepeople.android.app.structures.Coordinates;
import morepeople.android.app.structures.Participant;
import morepeople.android.app.structures.SearchEntry;
import morepeople.android.app.structures.UserState;

/**
 * Created by schreon on 3/22/14.
 */
public interface ICoreReadablePreferences {
    /**
     * @return the full USER_NAME of the user.
     */
    public String getUserName();

    /**
     * @return the ID of the user.
     */
    public String getUserId();

    /**
     * @return the host USER_NAME of the server.
     */
    public String getServerHostName();

    /**
     * @return the last known coordinates of the client.
     */
    public Coordinates getLastKnownCoordinates();


    /**
     * @return the current user state
     */
    public UserState getCurrentUserState();


    public List<Participant> getParticipantList();


    public List<SearchEntry> getSearchEntryList();


    public String getMatchTag();

}
