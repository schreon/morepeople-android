package morepeople.android.app.morepeople.android.app.core;

import android.location.Location;

/**
 * Created by schreon on 3/20/14.
 */
public interface ICoreLocation {
    public Location getLastKnownLocation();
    public void setLocationUpdateHandler(IDataCallback onLocationUpdate);
    public void setPolling(boolean isPolling);
}
