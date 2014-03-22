package morepeople.android.app.factory;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Map;

import morepeople.android.app.CancelActivity;
import morepeople.android.app.ChatActivity;
import morepeople.android.app.ConfirmationActivity;
import morepeople.android.app.EvaluationActivity;
import morepeople.android.app.SearchActivity;
import morepeople.android.app.controller.CoreModelController;
import morepeople.android.app.controller.CoreViewController;
import morepeople.android.app.core.CoreApi;
import morepeople.android.app.core.CoreClient;
import morepeople.android.app.core.CoreLocationManager;
import morepeople.android.app.core.CorePreferences;
import morepeople.android.app.core.CoreRegistrar;
import morepeople.android.app.core.CoreStateHandler;
import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.IApiCallback;
import morepeople.android.app.interfaces.ICoreApi;
import morepeople.android.app.interfaces.ICoreClient;
import morepeople.android.app.interfaces.ICoreModelController;
import morepeople.android.app.interfaces.ICoreViewController;
import morepeople.android.app.structures.Coordinates;
import morepeople.android.app.interfaces.ICallback;
import morepeople.android.app.interfaces.ICoreFactory;
import morepeople.android.app.interfaces.ICoreLocationManager;
import morepeople.android.app.interfaces.ICorePreferences;
import morepeople.android.app.interfaces.ICoreRegistrar;
import morepeople.android.app.interfaces.ICoreStateHandler;
import morepeople.android.app.interfaces.IDataCallback;
import morepeople.android.app.interfaces.IErrorCallback;
import morepeople.android.app.structures.UserState;

/**
 * Created by schreon on 3/22/14.
 */
public class CoreFactory implements ICoreFactory {

    private static final String TAG = "morepeople.android.app.factory.CoreFactory";
    private Context context;

    public CoreFactory(Context context) {
        this.context = context;
    }

    @Override
    public void createCoreApi(ICallback onNoUserNameFound, final IApiCallback onFinish, final IErrorCallback onError) {
        final Plan plan = new Plan();

        final ICorePreferences preferences = new CorePreferences(context);

        /**
         * Step 1: Retrieve user USER_ID from GCM if necessary
         */
        if (preferences.getUserId() == null) {
            plan.addStep(new ICallback() {
                @Override
                public void run() {
                    ICoreRegistrar coreRegistrar = new CoreRegistrar(context);

                    coreRegistrar.register(new IDataCallback() {
                        @Override
                        public void run(Map<String, Object> data) {
                            preferences.setUserId((String) data.get(Constants.PROPERTY_USER_ID));
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

        /**
         * Retrieve hostname
         */
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String hostName = (String) ai.metaData.get(Constants.PROPERTY_HOSTNAME);
        preferences.setServerHostName(hostName);

        /**
         *  Create client
         */
        final ICoreClient client = new CoreClient(hostName);

        final ICoreModelController coreModelController = new CoreModelController();
        final ICoreViewController coreViewController = new CoreViewController(context);

        /**
         * Step 3: server response handler which updates the preferences
         */
        final IDataCallback onServerResponse = new IDataCallback() {
            @Override
            public void run(Map<String, Object> data) {
                coreModelController.handleResponse(data, preferences);
                coreViewController.handleUpdate(preferences);
            }
        };

        plan.addStep(new ICallback() {
            @Override
            public void run() {
                ICoreApi coreApi = new CoreApi(client, preferences, onServerResponse);
                onFinish.run(coreApi);
            }
        });

        plan.start();
    }
}
