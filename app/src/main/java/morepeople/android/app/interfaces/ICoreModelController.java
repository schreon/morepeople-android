package morepeople.android.app.interfaces;

import java.util.Map;

/**
 * Response Handler for server responses. Updates the preferences accordingly.
 */
public interface ICoreModelController {
    public void handleResponse(Map<String, Object> data, ICoreWritablePreferences preferences);
}
