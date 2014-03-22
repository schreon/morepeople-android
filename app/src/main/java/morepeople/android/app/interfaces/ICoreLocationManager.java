package morepeople.android.app.interfaces;

import android.location.Location;

/**
 * Location Manager which provides access to the location of the user.
 */
public interface ICoreLocationManager {
    public Coordinates getLastKnownCoordinates();

    /**
     * @param onLocationUpdate will be called when the location changes. The parameter is an
     *                         object of type Location.
     */
    public void setLocationUpdateHandler(IDataCallback onLocationUpdate);

    /**
     * Turn listening to location updates on and off.
     * @param isListening determines if location updates are observed or not.
     */
    public void setListenToLocationUpdates(boolean isListening);
}
