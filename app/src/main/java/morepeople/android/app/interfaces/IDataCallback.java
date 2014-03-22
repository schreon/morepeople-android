package morepeople.android.app.interfaces;

import java.util.Map;

/**
 * A callback with a data parameter.
 */
public interface IDataCallback {
    /**
     * @param data key-value map with the returned data.
     */
    public void run(Map<String, Object> data);
}
