package morepeople.android.app.interfaces;

/**
 * Created by schreon on 3/22/14.
 */
public interface ICoreFactory {
    public ICoreClient createCoreClient();
    public ICoreLocationManager createCoreLocationManager();
    public ICoreRegistrar createCoreRegistrar();
    public ICoreStateHandler createCoreStateHandler();
    public ICorePreferencesManager createCoreUserInfoManager();
}
