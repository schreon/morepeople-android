package morepeople.android.app.controller;

import android.util.Log;

import java.util.List;
import java.util.Map;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.ICoreModelController;
import morepeople.android.app.interfaces.ICoreWritablePreferences;
import morepeople.android.app.structures.UserState;

/**
 * This controller handles model updates coming from the server.
 * <p/>
 * Contract: This is the only class which directly deals with the dictionary coming from
 * server responses. It should be considered together with the server implementation.
 * It relies on the data having the right format!
 */
public class CoreModelController implements ICoreModelController {

    private static final String TAG = "morepeople.android.app.controller.CoreModelController";

    @Override
    public void handleResponse(Map<String, Object> data, ICoreWritablePreferences preferences) {
        Log.d(TAG, data.toString());
        if (data.keySet().contains(Constants.PROPERTY_STATE)) {
            // Update the user state
            String strState = (String) data.get(Constants.PROPERTY_STATE);
            UserState userState = UserState.valueOf(strState);
            preferences.setCurrentUserState(userState);
        }

        if (data.keySet().contains(Constants.PROPERTY_MATCH_TAG)) {
            // Update match tag
            String matchTag = (String) data.get(Constants.PROPERTY_MATCH_TAG);
            preferences.setMatchTag(matchTag);
        }

        if (data.keySet().contains(Constants.PROPERTY_PARTICIPANTS)) {
            // Update match tag
            List participantsList = (List) data.get(Constants.PROPERTY_PARTICIPANTS);
            preferences.setParticipantListFromMap(participantsList);
        }

        if (data.keySet().contains(Constants.PROPERTY_SEARCHENTRIES)) {
            // Update match tag
            List searchEntryList = (List) data.get(Constants.PROPERTY_SEARCHENTRIES);
            preferences.setSearchEntryListFromMap(searchEntryList);
        }
    }
}
