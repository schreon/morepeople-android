package morepeople.android.app;

import android.content.Context;
import android.content.SharedPreferences;

import org.robolectric.Robolectric;

/**
 * Created by schreon on 3/15/14.
 */
public class ApplicationTest {
    public static void sharedPrefs() {
        MainApplication.beforeClassJob = new Runnable() {
            @Override
            public void run() {
                // insert reg id, user name
                SharedPreferences sharedPreferences = Robolectric.application.getSharedPreferences("MorePeople", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("appUsername", "Thorsten Test").commit();
                sharedPreferences.edit().putString(MainRegistrar.PROPERTY_REG_ID, "test-gcm-id").commit();
            }
        };
    }
}
