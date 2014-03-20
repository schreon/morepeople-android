package morepeople.android.app;

import java.util.HashMap;

/**
 * Created by schreon on 3/20/14.
 */
public interface ICoreClient {
    public void doGetRequest(String path, HashMap<String, String> data, IDataCallback onSuccess, IDataCallback onError);
    public void doPostRequest(String path, HashMap<String, String> data, IDataCallback onSuccess, IDataCallback onError);
}
