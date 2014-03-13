package morepeople.android.app;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by schreon on 3/12/14.
 */
@SuppressWarnings("ConstantConditions")
@Config(emulateSdk = 18)
@RunWith(TestRunner.class)
/**
 * Created by schreon on 3/4/14.
 */
public class GcmBroadcastReceiverTest {
    /**
     * Setup method
     */
    @Before
    public void setUp(){
    }

    @Test
    public void shouldNotBeNull() {
    }

    @Test
    public void shouldStartIntentService() {
        Intent intent = new Intent(Robolectric.application, GcmIntentService.class);
        GcmIntentService service = new GcmIntentService();
        service.onCreate();
        service.onStartCommand(intent, 0, 42);
        service.onDestroy();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(Robolectric.application);
        String messageType = gcm.getMessageType(intent);
    }

    /**
     * TODO: If a user sends a chat message, update
     */
}