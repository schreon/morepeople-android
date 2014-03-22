package morepeople.android.app.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import morepeople.android.app.interfaces.ICoreClient;
import morepeople.android.app.interfaces.ICoreFactory;
import morepeople.android.app.interfaces.ICoreLocationManager;
import morepeople.android.app.interfaces.ICorePreferencesManager;
import morepeople.android.app.interfaces.ICoreRegistrar;
import morepeople.android.app.interfaces.ICoreStateHandler;

/**
 * Created by schreon on 3/22/14.
 */
public class CoreFactory implements ICoreFactory{

    private Context context;
    public CoreFactory(Context context) {
        this.context = context;
    }

    @Override
    public ICoreClient createCoreClient() {
        return null;
    }

    @Override
    public ICoreLocationManager createCoreLocationManager() {
        return null;
    }

    @Override
    public ICoreRegistrar createCoreRegistrar() {
        return null;
    }

    @Override
    public ICoreStateHandler createCoreStateHandler() {
        return null;
    }

    @Override
    public ICorePreferencesManager createCoreUserInfoManager() {
        return null;
    }

    private String readHostName() {
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) ai.metaData.get("morepeople.android.app.HOSTNAME");
    }
}
