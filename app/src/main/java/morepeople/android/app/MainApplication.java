package morepeople.android.app;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.util.Map;

/**
 * The core application.
 *
 * TODO:
 * use the broadcast system to request states
 * and
 */
public class MainApplication extends Application {
    private static MainApplication instance;
    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static void handleStateTransition(Map<String, String> stateMap) {
        // TODO
    }


}
