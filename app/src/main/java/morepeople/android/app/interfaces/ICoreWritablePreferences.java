package morepeople.android.app.interfaces;

import java.util.List;
import java.util.Map;

import morepeople.android.app.structures.Coordinates;
import morepeople.android.app.structures.Participant;
import morepeople.android.app.structures.SearchEntry;
import morepeople.android.app.structures.UserState;

/**
 * Provides access to persisted preferences.
 */
public interface ICoreWritablePreferences extends ICoreReadablePreferences {
    /**
     * @param userName the new user USER_NAME.
     */
    public void setUserName(String userName);

    /**
     * @param userId the new user USER_ID.
     */
    public void setUserId(String userId);

    /**
     * @param serverHostName the new server host USER_NAME.
     */
    public void setServerHostName(String serverHostName);

    /**
     * @param lastKnownCoordinates the new last known location.
     */
    public void setLastKnownCoordinates(Coordinates lastKnownCoordinates);

    /**
     * @param currentUserState the new current user state.
     */
    public void setCurrentUserState(UserState currentUserState);

    public void setParticipantList(List<Participant> participantList);

    public void setParticipantListFromMap(List<Map> participantList);

    public void setSearchEntryList(List<SearchEntry> searchEntryList);

    public void setSearchEntryListFromMap(List<Map> searchEntryList);

    public void setMatchTag(String matchTag);
}
