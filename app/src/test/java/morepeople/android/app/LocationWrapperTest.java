package morepeople.android.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContext;
import org.robolectric.shadows.ShadowLocationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        Location fakeLocation = new Location(LocationManager.NETWORK_PROVIDER);
        fakeLocation.setLongitude(123);
        fakeLocation.setLatitude(456);

        slm.setLastKnownLocation(LocationManager.NETWORK_PROVIDER, fakeLocation);

        LocationResponseHandler locationResponseHandler = new LocationResponseHandler(){
            @Override
            public void gotInstantTemporaryLocation(Location location) {
                assertNotNull(location);
                checkMap.put("gotInstantTemporaryLocation", true);
            }

            @Override
            public void gotFallbackLocation(Location location) {
                assertNotNull(location);
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

        //sleep(50); // this is very ugly and should be refactored
        Robolectric.runUiThreadTasksIncludingDelayedTasks(); // wait for all async tasks to finish
        Robolectric.runBackgroundTasks();

        assertTrue(checkMap.get("gotFallbackLocation"));
    }
}
