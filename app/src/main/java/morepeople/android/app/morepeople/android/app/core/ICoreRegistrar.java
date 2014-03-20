package morepeople.android.app.morepeople.android.app.core;

/**
 * Created by schreon on 3/20/14.
 */
public interface ICoreRegistrar {
    public static final String PROPERTY_REG_ID = "REG_ID";
    public static final String PROPERTY_APP_VERSION = "APP_VERSION";
    public static final String SENDER_ID = "1039190776751";

    public String getRegistrationId();
    public void requestRegistrationId(IDataCallback onSuccess, IDataCallback onError);
}
