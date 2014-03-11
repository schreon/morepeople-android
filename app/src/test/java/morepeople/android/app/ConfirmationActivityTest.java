package morepeople.android.app;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by schreon on 3/3/14.
 */

@SuppressWarnings("ConstantConditions")
@Config(emulateSdk = 18)
@RunWith(TestRunner.class)
public class ConfirmationActivityTest {
    private ConfirmationActivity activity;

    /**
     * Setup method
     */
    @Before
    public void setUp(){
        activity = Robolectric.buildActivity(ConfirmationActivity.class).create().get();
    }

    @Test
    public void activityShouldNotBeNull() {
        assertNotNull(activity);
        assertNotNull(activity.findViewById(R.id.button_confirm));
        assertNotNull(activity.findViewById(R.id.button_reject));
    }

    @Test
    public void shouldDisplayParticipants() {
        ListView confirmListView = (ListView) activity.findViewById(R.id.confirm_list_view);
        assertNotNull(confirmListView);

        ParticipantsAdapter participantsAdapter = activity.getParticipantsAdapter();
        assertNotNull(participantsAdapter);

        // view participant if added
        for (int i=0; i < 3; i++) {
            Participant participant = new Participant("fake_device_"+String.valueOf(i), "Hans Dampf", "OPEN");
            participantsAdapter.add(participant);
            assertTrue(participantsAdapter.getCount() > 0);

            // update robolectric
            Robolectric.shadowOf(confirmListView).populateItems();

            // searchVview should have at least 1 child now
            assertTrue(confirmListView.getChildCount() > 0);

            boolean isDisplayed = false;
            // the ListView should now display the new message
            for (int j=0; j < confirmListView.getChildCount(); j++) {
                LinearLayout group = (LinearLayout)confirmListView.getChildAt(j);

                Participant foundParticipant = (Participant) group.getTag();
                assertNotNull(foundParticipant);

                assertTrue(group.getChildCount() > 0);

                String foundName = ((TextView)group.getChildAt(0)).getText().toString();
                String foundStatus = ((TextView)group.getChildAt(1)).getText().toString();

                if (!foundParticipant.id.equals(participant.id)) continue;
                if (!foundName.equals(participant.name)) continue;
                if (!foundStatus.equals(participant.status)) continue;

                isDisplayed = true;
            }
            assertTrue(isDisplayed);
        }
    }
}
