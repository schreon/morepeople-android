package morepeople.android.app.morepeople.android.app.core;

import android.location.Location;

/**
 * Created by schreon on 3/20/14.
 */
public interface ICoreUserInfo {
    public void load(IDataCallback onSuccess, IDataCallback onError);

    public String getUserName();

    public String getUserId();

    public Location getUserLocation();
}
