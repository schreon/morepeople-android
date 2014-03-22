package morepeople.android.app.factory;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Map;

import morepeople.android.app.CancelActivity;
import morepeople.android.app.ChatActivity;
import morepeople.android.app.ConfirmationActivity;
import morepeople.android.app.EvaluationActivity;
import morepeople.android.app.SearchActivity;
import morepeople.android.app.core.CoreLocationManager;
import morepeople.android.app.core.CorePreferences;
import morepeople.android.app.core.CoreRegistrar;
import morepeople.android.app.core.CoreStateHandler;
import morepeople.android.app.factory.Plan;
import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.Coordinates;
import morepeople.android.app.interfaces.ICallback;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.ICoreClient;
import morepeople.android.app.interfaces.ICoreFactory;
import morepeople.android.app.interfaces.ICoreLocationManager;
import morepeople.android.app.interfaces.ICorePreferences;
import morepeople.android.app.interfaces.ICoreRegistrar;
import morepeople.android.app.interfaces.ICoreStateHandler;
import morepeople.android.app.interfaces.IDataCallback;
import morepeople.android.app.interfaces.IErrorCallback;
import morepeople.android.app.interfaces.UserState;

/**
 * Created by schreon on 3/22/14.
 */
public class CoreFactory implements ICoreFactory {

    private static final String TAG = "morepeople.android.app.factory.CoreFactory";
    private Context context;

    public CoreFactory(Context context) {
        this.context = context;
    }

    // TODO: move to some kind of controller class?
    private synchronized void startActivityOnStateChange(UserState newState) {
        Intent intent;
        switch (newState) {
            case OFFLINE:
                Log.d(TAG, "SearchActivity");
                intent = new Intent(context, SearchActivity.class);
                break;
            case QUEUED:
                Log.d(TAG, "launch SearchActivity");
                intent = new Intent(context, SearchActivity.class);
                break;
            case OPEN:
                Log.d(TAG, "launch ConfirmationActivity");
                intent = new Intent(context, ConfirmationActivity.class);
                break;
            case ACCEPTED:
                Log.d(TAG, "launch ConfirmationActivity");
                intent = new Intent(context, ConfirmationActivity.class);
                break;
            case RUNNING:
                Log.d(TAG, "launch ChatActivity");
                intent = new Intent(context, ChatActivity.class);
                break;
            case FINISHED:
                Log.d(TAG, "launch EvaluationActivity");
                intent = new Intent(context, EvaluationActivity.class);
                break;
            case CANCELLED:
                Log.d(TAG, "launch CancelActivity");
                intent = new Intent(context, CancelActivity.class);
                break;
            default:
                Log.d(TAG, "invalid state");
                intent = new Intent(context, CancelActivity.class);
                break;
        }
        // put the state into the intent
        intent.putExtra(Constants.PROPERTY_STATE, newState.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void createCoreApi(ICallback onNoUserNameFound, final ICallback onFinish, final IErrorCallback onError) {
        final Plan plan = new Plan(onFinish);

        final ICorePreferences preferences = new CorePreferences(context);

        /**
         * Step 1: Retrieve user id from GCM if necessary
         */
        if ( preferences.getUserId() == null ) {
            plan.addStep(new ICallback() {
                @Override
                public void run() {
                    ICoreRegistrar coreRegistrar = new CoreRegistrar(context);

                    coreRegistrar.register(new IDataCallback() {
                        @Override
                        public void run(Map<String, Object> data) {
                            preferences.setUserId((String)data.get(Constants.PROPERTY_USER_ID));
                            plan.next();
                        }
                    }, onError);
                }
            });
        }

        /**
         * Step 2: Retrieve location if necessary
         */
        if (preferences.getLastKnownCoordinates() == null) {
            // create location manager
            final ICoreLocationManager coreLocationManager = new CoreLocationManager(context);
            Coordinates lastKnownCoordinates = coreLocationManager.getLastKnownCoordinates();
            if (lastKnownCoordinates != null) {
                preferences.setLastKnownCoordinates(lastKnownCoordinates);
            } else {
                plan.addStep(new ICallback() {
                    @Override
                    public void run() {
                       coreLocationManager.setLocationUpdateHandler(new IDataCallback() {
                           @Override
                           public void run(Map<String, Object> data) {
                               coreLocationManager.setListenToLocationUpdates(false);
                               Coordinates newCoordinates = (Coordinates) data.get(Constants.PROPERTY_COORDINATES);
                               preferences.setLastKnownCoordinates(newCoordinates);
                               plan.next();
                           }
                       });
                        coreLocationManager.setListenToLocationUpdates(true);
                    }
                });
            }
        }

        ICoreStateHandler coreStateHandler = new CoreStateHandler(preferences.getCurrentUserState());
        /**
         * Step 3: Connect state handler with activity dispatcher method
         */
        for (final UserState state : UserState.values()) {
            coreStateHandler.addEnterStateListener(state, new ICallback() {
                @Override
                public void run() {
                    preferences.setCurrentUserState(state);
                    startActivityOnStateChange(state);
                }
            });
        }

        plan.start();
    }
}
