package morepeople.android.app.interfaces;

import java.util.Map;

/**
 * Client to handle simplified HTTP GET and POST requests. The data exchange format is always
 * a Map<String, Object>.
 */
public interface ICoreClient {
    /**
     * @param path
     * @param arguments parameters. Plain String-String Map.
     * @param onSuccess
     * @param onError
     */
    public void doGetRequest(String path, Map<String, String> arguments, IDataCallback onSuccess, IErrorCallback onError);

    public void doPostRequest(String path, Map<String, Object> data, IDataCallback onSuccess, IErrorCallback onError);
}
