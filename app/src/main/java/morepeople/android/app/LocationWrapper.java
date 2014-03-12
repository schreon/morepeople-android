package morepeople.android.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * LocationWrapper provides functions for locations and providers
 */
public class LocationWrapper {

    private LocationResponseHandler locationResponseHandler;
    private LocationManager locationManager;
    private boolean gpsEnabled = false;
    private boolean networkEnabled = false;
    private Timer fallbackTimer;
    private Handler timerHandler;

    /**
     * locationListener provides mtehods for providers
     */
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            locationResponseHandler.gotNewLocation(location);
            try {
                fallbackTimer.cancel();
                locationManager.removeUpdates(this);
            } catch (Exception e) {

            }
        }

        /**
         * provider status changed
         * @param provider
         * @param i
         * @param bundle
         */
        @Override
        public void onStatusChanged(String provider, int i, Bundle bundle) {}

        /**
         * provider is activated
         * @param provider
         */
        @Override
        public void onProviderEnabled(String provider) {}

        /**
         * provider is disabled
         * @param provider
         */
        @Override
        public void onProviderDisabled(String provider) {}
    };

    /**
     * Runnable lastKnownLocationFallback
     */
    private final Runnable lastKnownLocationFallback = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();

            locationManager.removeUpdates(locationListener);

            locationResponseHandler.gotFallbackLocation(getLastKnownLocation());

            Looper.loop();
        }
    };

    /**
     * Constructor of LocationWrapper class
     */
    public LocationWrapper() {
        timerHandler = new Handler();
    }

    /**
     * Get the last known location
     * @return last known location
     */
    private Location getLastKnownLocation() {
        Location gpsLocation = null;
        Location networkLocation = null;

        if (gpsEnabled) {
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (networkEnabled) {
            networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        // If both values are available, use the newest one
        if (gpsLocation != null && networkLocation != null) {
            if(gpsLocation.getTime() > networkLocation.getTime()) {
                return gpsLocation;
            } else {
                return networkLocation;
            }
        }

        if(gpsLocation != null) {
            return gpsLocation;
        }

        if(networkLocation != null) {
            return networkLocation;
        }

        return null;
    }

    /**
     * Request location
     * @param context
     * @param locationResponseHandler
     * @param waitMillis
     */
    public void requestLocation(Context context, LocationResponseHandler locationResponseHandler, long waitMillis) {
        this.locationResponseHandler = locationResponseHandler;

        // TODO: kick off location request

        // Get the location manager from the context if it is not already set.
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        // find out if gps is enabled
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // find out if network is enabled
        networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // don't do anything if no location provider is enabled
        if (!gpsEnabled && !networkEnabled) {
            return;
        }

        // register the location listener both for GPS and network
        if (gpsEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        if (networkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        timerHandler.postDelayed(lastKnownLocationFallback, waitMillis);

        locationResponseHandler.gotInstantTemporaryLocation(getLastKnownLocation());
    }
}
