package morepeople.android.app.controller;

import java.util.Map;

import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.ICoreModelController;
import morepeople.android.app.interfaces.ICorePreferences;
import morepeople.android.app.structures.UserState;

/**
 * This controller handles model updates coming from the server.
 *
 * Contract: This is the only class which directly deals with the dictionary coming from
 * server responses. It should be considered together with the server implementation.
 * It relies on the data having the right format!
 */
public class CoreModelController implements ICoreModelController {

    @Override
    public void handleResponse(Map<String, Object> data, ICorePreferences preferences) {
        // Update the user state
        String strState = (String) data.get(Constants.PROPERTY_STATE);
        UserState userState = UserState.valueOf(strState);
        preferences.setCurrentUserState(userState);

        switch (userState) {
            case OFFLINE:

                break;
            case QUEUED:

                break;
            case OPEN:

                break;
            case ACCEPTED:

                break;
            case RUNNING:

                break;
            case FINISHED:

                break;
            case CANCELLED:

                break;
            default:

                break;
        }
    }
}
