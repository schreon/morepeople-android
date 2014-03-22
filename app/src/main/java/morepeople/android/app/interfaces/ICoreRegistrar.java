package morepeople.android.app.interfaces;

/**
 * Registrar which provides access to all registration processes.
 */
public interface ICoreRegistrar {
    public static final String PROPERTY_REG_ID = "REG_ID";
    public static final String PROPERTY_APP_VERSION = "APP_VERSION";
    public static final String SENDER_ID = "1039190776751";

    public String getRegistrationId();

    /**
     * Register the user with the service.
     * @param onFinish will be called when the registration process is finished.
     * @param onError will be called when there's an error during the registration process.
     */
    public void register(ICallback onFinish, IDataCallback onError);
}
