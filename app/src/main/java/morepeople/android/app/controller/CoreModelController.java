package morepeople.android.app.controller;

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

    @Override
    public void handleResponse(Map<String, Object> data, ICoreWritablePreferences preferences) {
        // Update the user state
        String strState = (String) data.get(Constants.PROPERTY_STATE);
        UserState userState = UserState.valueOf(strState);
        preferences.setCurrentUserState(userState);

        switch (userState) {
            case QUEUED: {
                String matchTag = (String) data.get(Constants.PROPERTY_MATCH_TAG);
                preferences.setMatchTag(matchTag);
            }
            break;
            case OPEN: {
                String matchTag = (String) data.get(Constants.PROPERTY_MATCH_TAG);
                preferences.setMatchTag(matchTag);
                String strParticipants = (String) data.get(Constants.PROPERTY_PARTICIPANTS);
                // directly pass serialized string
                preferences.setParticipantList(strParticipants);
            }
            break;
            case ACCEPTED:
                String matchTag = (String) data.get(Constants.PROPERTY_MATCH_TAG);
                preferences.setMatchTag(matchTag);
                String strParticipants = (String) data.get(Constants.PROPERTY_PARTICIPANTS);
                // directly pass serialized string
                preferences.setParticipantList(strParticipants);
                break;
            case RUNNING:
                // TODO
                break;
            case FINISHED:
                // TODO
                break;
            case CANCELLED:
                //String serverMessage = (String)data.get(Constants.PROPERTY_SERVERMESSAGE);
                // TODO
                break;
            default:
                break;
        }
    }
}
