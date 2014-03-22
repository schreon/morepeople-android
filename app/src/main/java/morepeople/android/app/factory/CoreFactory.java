package morepeople.android.app.factory;

import android.content.Context;

import java.util.Map;

import morepeople.android.app.core.CoreLocationManager;
import morepeople.android.app.core.CorePreferences;
import morepeople.android.app.core.CoreRegistrar;
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
import morepeople.android.app.interfaces.IDataCallback;
import morepeople.android.app.interfaces.IErrorCallback;

/**
 * Created by schreon on 3/22/14.
 */
public class CoreFactory implements ICoreFactory {

    private Context context;

    public CoreFactory(Context context) {
        this.context = context;
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

        plan.start();
    }
}
