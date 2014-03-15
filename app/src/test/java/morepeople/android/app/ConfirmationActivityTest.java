package morepeople.android.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import org.robolectric.shadows.ShadowAlertDialog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.robolectric.Robolectric.shadowOf;

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
        // insert reg id, user name
        SharedPreferences sharedPreferences = Robolectric.application.getSharedPreferences("MorePeople", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("appUsername", "Thorsten Test").commit();
        sharedPreferences.edit().putString(MainRegistrar.PROPERTY_REG_ID, "test-gcm-id").commit();
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
            shadowOf(confirmListView).populateItems();

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

        // if the user clicks "confirm", the button layout should be "gone" and the
        // wait layout should become visible

        LinearLayout layoutConfirmWait = (LinearLayout) activity.findViewById(R.id.confirm_wait_layout);
        LinearLayout layoutConfirmButtons = (LinearLayout) activity.findViewById(R.id.confirm_button_layout);
        Button buttonConfirm = (Button) activity.findViewById(R.id.button_confirm);

        assertEquals(layoutConfirmWait.getVisibility(), View.GONE);
        assertEquals(layoutConfirmButtons.getVisibility(), View.VISIBLE);

        buttonConfirm.performClick();

        assertEquals(layoutConfirmWait.getVisibility(), View.VISIBLE);
        assertEquals(layoutConfirmButtons.getVisibility(), View.GONE);
    }

    // TODO: if the user clicks the button "reject", an alert dialog should be shown
    // TODO: if the user clicks "okay" on the alert dialog, he should be directed to the search activity
    @Test
    public void shouldDirectToStartOnReject() {
        Button buttonReject = (Button) activity.findViewById(R.id.button_reject);
        buttonReject.performClick();

        AlertDialog alert =
                ShadowAlertDialog.getLatestAlertDialog();

        ShadowAlertDialog sAlert = shadowOf(alert);
        assertNotNull(sAlert);
        assertThat(sAlert.getTitle().toString(),
                equalTo(activity.getString(R.string.please_confirm_cancel)));

        alert.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
        assertNull(shadowOf(activity).getNextStartedActivity());

        // now do it actually
        buttonReject.performClick();
        alert = ShadowAlertDialog.getLatestAlertDialog();

        alert.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

        sAlert = shadowOf(alert);
        assertNotNull(sAlert);
        assertThat(sAlert.getTitle().toString(),
                equalTo(activity.getString(R.string.please_confirm_cancel)));

        Intent intent = shadowOf(activity).peekNextStartedActivity();
        assertEquals(SearchActivity.class.getCanonicalName(), intent.getComponent().getClassName());
    }

    /**
     * TODO: If a GCM message is received, indicating another user confirmed, update the activity.
     */
    @Test
    public void shouldUpdateOnConfirmation() {

    }
}
