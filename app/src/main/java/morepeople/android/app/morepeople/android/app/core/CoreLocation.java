package morepeople.android.app.morepeople.android.app.core;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by schreon on 3/20/14.
 */
public class CoreLocation implements ICoreLocation{
    private LocationManager locationManager;
    private boolean gpsEnabled;
    private boolean networkEnabled;
    private IDataCallback onLocationUpdate;
    private boolean isPolling;
    private Context context;

    private LocationListener gpsLocationListener;
    private LocationListener networkLocationListener;

    public CoreLocation(Context context) {
        onLocationUpdate = null;
        gpsEnabled = false;
        networkEnabled = false;
        isPolling = false;
        this.context = context;

        gpsLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if ( onLocationUpdate != null) {
                    onLocationUpdate.run(location);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        networkLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if ( onLocationUpdate != null) {
                    onLocationUpdate.run(location);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    @Override
    public Location getLastKnownLocation() {
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        // find out if gps is enabled
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // find out if network is enabled
        networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

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

    @Override
    public void setLocationUpdateHandler(IDataCallback onLocationUpdate) {
        this.onLocationUpdate = onLocationUpdate;
    }

    @Override
    public void setPolling(boolean isPolling) {
        if (!this.isPolling && isPolling) {
            turnOnLocationPolling();
        }
        if(this.isPolling && !isPolling) {
            turnOffLocationPolling();
        }
        this.isPolling = isPolling;
    }

    private void turnOnLocationPolling() {
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
        }
        if (networkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationListener);
        }
    }

    private void turnOffLocationPolling() {
        locationManager.removeUpdates(gpsLocationListener);
        locationManager.removeUpdates(networkLocationListener);
    }


}
