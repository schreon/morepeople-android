package morepeople.android.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLocationManager;

import java.util.HashMap;
import java.util.Map;

import morepeople.android.app.core.CoreLocationManager;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.ICoreLocationManager;
import morepeople.android.app.interfaces.ICoreRegistrar;
import morepeople.android.app.interfaces.IDataCallback;
import morepeople.android.app.structures.UserState;

import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertSame;
import static org.robolectric.Robolectric.shadowOf;

/**
 * Created by schreon on 3/3/14.
 */

@SuppressWarnings("ConstantConditions")
@Config(emulateSdk = 18)
@RunWith(TestRunner.class)
/**
 * Created by schreon on 3/4/14.
 */
public class CoreLocationTest {
    ICoreLocationManager coreLocation;

    public static void sharedPrefs() {
        // Insert registration id and the user name into SharedPreferences
        SharedPreferences sharedPreferences = Robolectric.application.getSharedPreferences(ICoreApi.SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("appUsername", "Thorsten Test").commit();
        sharedPreferences.edit().putString(ICoreRegistrar.PROPERTY_REG_ID, "test-gcm-id").commit();

        // Add pending HTTP response which will be served as soon as the application
        // sends the first HTTP request (no matter which request that will be).
        Robolectric.addPendingHttpResponse(200, "{ 'STATE' : '"+ UserState.RUNNING.toString()+"' }");
    }

    /**
     * Setup method
     */
    @Before
    public void setUp(){
        sharedPrefs();
        coreLocation = new CoreLocationManager(Robolectric.application);
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(coreLocation);
    }

    @Test
    public void shouldProvideFallbackLocation() throws InterruptedException {
        final Map<String, Boolean> checkMap = new HashMap<String, Boolean>();

        Context context = Robolectric.application;

        LocationManager instanceOfLocationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        ShadowLocationManager slm = shadowOf(instanceOfLocationManager);
        slm.setProviderEnabled(LocationManager.NETWORK_PROVIDER, true);

        final Location fakeLocation = new Location(LocationManager.NETWORK_PROVIDER);
        fakeLocation.setLongitude(123);
        fakeLocation.setLatitude(456);

        slm.setLastKnownLocation(LocationManager.NETWORK_PROVIDER, fakeLocation);

        Location retrievedLocation = coreLocation.getLastKnownLocation();
        assertNotNull(retrievedLocation);
        assertEquals(fakeLocation, retrievedLocation);
    }

    @Test
    public void shouldProvideNewLocation() throws InterruptedException {
        // The asynchronous nature of LocationWrapper requires an object where assertion results
        // can be stored. This is necessary, because it could not be determined if an event
        // that should be reached (and thus does not fail) has actually been reached.
        final Map<String, Boolean> assertionMap = new HashMap<String, Boolean>();

        // During robolectric tests, no real device is available. Thus, system services like
        // the NETWORK_PROVIDER and the according LocationManager must be mocked.
        LocationManager instanceOfLocationManager = (LocationManager) Robolectric.application.getSystemService(Context.LOCATION_SERVICE);
        ShadowLocationManager shadowLocationManager = shadowOf(instanceOfLocationManager);
        shadowLocationManager.setProviderEnabled(LocationManager.NETWORK_PROVIDER, true);

        final Location currentLocation = new Location(LocationManager.NETWORK_PROVIDER);
        currentLocation.setLongitude(123);
        currentLocation.setLatitude(456);
        // Attention: Android won't trigger a changed location event if the time span between
        // the location objects is too small!!!
        currentLocation.setTime(0);

        final Location newLocation = new Location(LocationManager.NETWORK_PROVIDER);
        newLocation.setLongitude(666);
        newLocation.setLatitude(333);
        // Attention: Android won't trigger a changed location event if the time span between
        // the location objects is too small!!!
        newLocation.setTime(1000000);

        shadowLocationManager.setLastKnownLocation(LocationManager.NETWORK_PROVIDER, currentLocation);

        coreLocation.setLocationUpdateHandler(new IDataCallback() {
            @Override
            public void run(Object data) {
                Location location = (Location) data;
                assertNotNull(location);
                assertEquals(newLocation, location);
                assertionMap.put("gotNewLocation", true);
            }
        });

        // This starts the asynchronous location request
        coreLocation.setListenToLocationUpdates(true);

        // This simulates a location changed event
        shadowLocationManager.simulateLocation(newLocation);

        // Wait for all async tasks to finish
        Robolectric.runUiThreadTasks();

        // Assert that the correct asynchronous event handlers have been called
        assertTrue(assertionMap.keySet().contains("gotNewLocation"));

        // Immediately run the delayed fallback task if it still is existent
        Robolectric.runUiThreadTasksIncludingDelayedTasks();

        coreLocation.setListenToLocationUpdates(false);
    }
}
