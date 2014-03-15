package morepeople.android.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLocationManager;

import java.util.HashMap;
import java.util.Map;

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
public class LocationWrapperTest {
    SearchActivity activity;
    LocationWrapper locationWrapper;

    @BeforeClass
    public static void sharedPrefs() {
        MainApplication.initJob = new Runnable() {
            @Override
            public void run() {
                // insert reg id, user name
                SharedPreferences sharedPreferences = Robolectric.application.getSharedPreferences("MorePeople", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("appUsername", "Thorsten Test").commit();
                sharedPreferences.edit().putString(MainRegistrar.PROPERTY_REG_ID, "test-gcm-id").commit();

                ApplicationInfo ai = null;
                try {
                    ai = Robolectric.application.getPackageManager().getApplicationInfo(MainApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String hostName = (String) ai.metaData.get("morepeople.android.app.HOSTNAME");

                // add HTTP request which will be
                Robolectric.addPendingHttpResponse(200, "{ 'STATE' : '"+MainApplication.UserState.OFFLINE.toString()+"' }");
            }
        };
    }

    /**
     * Setup method
     */
    @Before
    public void setUp(){
        activity = Robolectric.buildActivity(SearchActivity.class).create().get();
        locationWrapper = new LocationWrapper();
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(activity);
    }

    @Test
    public void shouldProvideFallbackLocation() throws InterruptedException {
        final Map<String, Boolean> checkMap = new HashMap<String, Boolean>();

        Context context = activity.getBaseContext();

        LocationManager instanceOfLocationManager = (LocationManager) Robolectric.application.getSystemService(context.LOCATION_SERVICE);
        ShadowLocationManager slm = shadowOf(instanceOfLocationManager);
        slm.setProviderEnabled(LocationManager.NETWORK_PROVIDER, true);

        final Location fakeLocation = new Location(LocationManager.NETWORK_PROVIDER);
        fakeLocation.setLongitude(123);
        fakeLocation.setLatitude(456);

        slm.setLastKnownLocation(LocationManager.NETWORK_PROVIDER, fakeLocation);

        LocationResponseHandler locationResponseHandler = new LocationResponseHandler(){
            @Override
            public void gotInstantTemporaryLocation(Location location) {
                assertNotNull(location);
                assertEquals(fakeLocation, location);
                checkMap.put("gotInstantTemporaryLocation", true);
            }

            @Override
            public void gotFallbackLocation(Location location) {
                assertNotNull(location);
                assertEquals(fakeLocation, location);
                // should not end up here
                checkMap.put("gotFallbackLocation", true);
            }

            @Override
            public void gotNewLocation(Location location) {
                // should not get here!
                fail();
            }
        };

        locationWrapper.requestLocation(activity.getBaseContext(),locationResponseHandler, 60000);

        assertTrue(checkMap.get("gotInstantTemporaryLocation"));

        Robolectric.runUiThreadTasksIncludingDelayedTasks(); // wait for all async tasks to finish

        assertTrue(checkMap.get("gotFallbackLocation"));
    }

    @Test
    public void shouldProvideNewLocation() throws InterruptedException {
        final Map<String, Boolean> checkMap = new HashMap<String, Boolean>();

        Context context = activity.getBaseContext();

        LocationManager instanceOfLocationManager = (LocationManager) Robolectric.application.getSystemService(Context.LOCATION_SERVICE);
        ShadowLocationManager slm = shadowOf(instanceOfLocationManager);
        slm.setProviderEnabled(LocationManager.NETWORK_PROVIDER, true);

        final Location fakeLocation = new Location(LocationManager.NETWORK_PROVIDER);
        fakeLocation.setLongitude(123);
        fakeLocation.setLatitude(456);
        fakeLocation.setTime(0);
        slm.setLastKnownLocation(LocationManager.NETWORK_PROVIDER, fakeLocation);


        final Location changedLocation = new Location(LocationManager.NETWORK_PROVIDER);
        changedLocation.setLongitude(666);
        changedLocation.setLatitude(333);
        changedLocation.setTime(1000000);

        LocationResponseHandler locationResponseHandler = new LocationResponseHandler(){
            @Override
            public void gotInstantTemporaryLocation(Location location) {
                assertNotNull(location);
                assertEquals(fakeLocation, location);
                checkMap.put("gotInstantTemporaryLocation", true);
            }

            @Override
            public void gotFallbackLocation(Location location) {
                // should not get here!
                checkMap.put("gotFallbackLocation", true);
            }

            @Override
            public void gotNewLocation(Location location) {
                assertNotNull(location);
                assertEquals(changedLocation, location);
                checkMap.put("gotNewLocation", true);
            }
        };

        locationWrapper.requestLocation(activity.getBaseContext(),locationResponseHandler, 60000);

        slm.simulateLocation(changedLocation);

        assertFalse(checkMap.keySet().contains("gotFallbackLocation"));
        assertTrue(checkMap.keySet().contains("gotInstantTemporaryLocation"));
        assertTrue(checkMap.keySet().contains("gotNewLocation"));
    }
}
