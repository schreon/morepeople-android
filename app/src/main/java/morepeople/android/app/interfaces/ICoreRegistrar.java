package morepeople.android.app.interfaces;

/**
 * Registrar which provides access to all registration processes.
 */
public interface ICoreRegistrar {

    /**
     * Register the user with the service.
     * @param onFinish will be called when the registration process is finished. Parameter: Map with reg id.
     * @param onError will be called when there's an error during the registration process.
     */
    public void register(IDataCallback onFinish, IErrorCallback onError);
}
