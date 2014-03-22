package morepeople.android.app.interfaces;

import java.util.Map;

/**
 * Created by schreon on 3/20/14.
 */
public interface ICoreClient {
    public void doGetRequest(String path, Map<String, String> data, IDataCallback onSuccess, IDataCallback onError);

    public void doPostRequest(String path, Map<String, Object> data, IDataCallback onSuccess, IDataCallback onError);
}
