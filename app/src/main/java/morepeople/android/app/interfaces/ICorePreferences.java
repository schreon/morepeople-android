package morepeople.android.app.interfaces;

import java.util.List;

import morepeople.android.app.structures.Coordinates;
import morepeople.android.app.structures.Participant;
import morepeople.android.app.structures.SearchEntry;
import morepeople.android.app.structures.UserState;

/**
 * Provides access to persisted preferences.
 */
public interface ICorePreferences {
    /**
     * @return the full USER_NAME of the user.
     */
    public String getUserName();

    /**
     * @param userName the new user USER_NAME.
     */
    public void setUserName(String userName);

    /**
     * @return the ID of the user.
     */
    public String getUserId();

    /**
     * @param userId the new user USER_ID.
     */
    public void setUserId(String userId);

    /**
     * @return the host USER_NAME of the server.
     */
    public String getServerHostName();

    /**
     * @param serverHostName the new server host USER_NAME.
     */
    public void setServerHostName(String serverHostName);

    /**
     * @return the last known coordinates of the client.
     */
    public Coordinates getLastKnownCoordinates();

    /**
     * @param lastKnownCoordinates the new last known location.
     */
    public void setLastKnownCoordinates(Coordinates lastKnownCoordinates);

    /**
     * @return the current user state
     */
    public UserState getCurrentUserState();

    /**
     * @param currentUserState the new current user state.
     */
    public void setCurrentUserState(UserState currentUserState);


    public List<Participant> getParticipantList();
    public void setParticipantList(List<Participant> participantList);
    public void setParticipantList(String strParticipantList);

    public List<SearchEntry> getSearchEntryList();
    public void setSearchEntryList(List<SearchEntry> searchEntryList);
    public void setSearchEntryList(String strSearchEntryList);

    public String getMatchTag();
    public void setMatchTag(String matchTag);
}
