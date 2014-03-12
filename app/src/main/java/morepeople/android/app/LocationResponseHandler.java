package morepeople.android.app;

import android.location.Location;

/**
 * LocationResponseHandler provides abstract functions for location handling
 */
public abstract class LocationResponseHandler {

    /**
     * got instant temporary location
     * @param location
     */
    public abstract void gotInstantTemporaryLocation(Location location);

    /**
     * got fallback location of no new location can be found
     * @param location
     */
    public abstract void gotFallbackLocation(Location location);

    /**
     * got new location
     * @param location
     */
    public abstract void gotNewLocation(Location location);
}
