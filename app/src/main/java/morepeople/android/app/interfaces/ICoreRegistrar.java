package morepeople.android.app.interfaces;

/**
 * Registrar which provides access to all registration processes.
 */
public interface ICoreRegistrar {

    /**
     * Contract: This may return null until register has been called.
     * @return the registration ID of the user.
     */
    public String getRegistrationId();

    /**
     * Register the user with the service.
     * @param onFinish will be called when the registration process is finished.
     * @param onError will be called when there's an error during the registration process.
     */
    public void register(ICallback onFinish, IDataCallback onError);
}
