package morepeople.android.app;

import android.location.Location;

/**
 * Created by schreon on 3/12/14.
 */
public abstract class LocationResponseHandler {

    public abstract void gotInstantTemporaryLocation(Location location);
    public abstract void gotFallbackLocation(Location location);
    public abstract void gotNewLocation(Location location);
}
