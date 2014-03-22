package morepeople.android.app.interfaces;

import android.location.Location;

/**
 * Provides access to all preferences:
 * user name
 * user id
 * host name of the server
 */
public interface ICorePreferencesManager {
    /**
     * Initialize.
     * @param onFinish will be called when initialization is complete.
     * @param onError will be called if an error happens during initialization.
     */
    public void initialize(ICallback onFinish, IDataCallback onError);

    /**
     * @return the full name of the user.
     */
    public String getUserName();

    /**
     * @return the ID of the user.
     */
    public String getUserId();

    /**
     * @return the host name of the server.
     */
    public String getServerHostName();
}
