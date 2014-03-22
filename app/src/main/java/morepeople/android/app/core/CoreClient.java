package morepeople.android.app.core;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import morepeople.android.app.interfaces.ICallback;
import morepeople.android.app.interfaces.ICoreClient;
import morepeople.android.app.interfaces.IDataCallback;
import morepeople.android.app.interfaces.IErrorCallback;

/**
 * Created by schreon on 3/20/14.
 */
public class CoreClient implements ICoreClient {
    private static final String TAG = "morepeople.android.app.core.CoreClient";
    /**
     * the host USER_NAME
     */
    private String hostName;

    /**
     * the http client
     */
    private HttpClient client;

    /**
     * new gson
     */
    private Gson gson = new Gson();

    public CoreClient(String hostName) {
        this.hostName = hostName;
        HttpParams httpParams = new BasicHttpParams();
        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(httpParams, HTTP.UTF_8);
        client = new DefaultHttpClient(httpParams);
    }

    private StringEntity toJson(Object object) {
        try {
            return new StringEntity(gson.toJson(object), HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Override
    public void doGetRequest(String path, Map<String, String> data, final IDataCallback onSuccess, final IErrorCallback onError) {

        // Url rewriting \m/
        String url = hostName + path;
        if (data != null) {
            if (data.size() > 0) {
                url += "?";
                for (String key : data.keySet()) {
                    url += key + "=" + data.get(key) + "&";
                }
            }
        }
        final HttpGet get = new HttpGet(url);
        get.setHeader("Accept", "application/json");
        get.setHeader("Content-type", "application/json");
        Log.d("CoreClient -> url", url);
        Log.d("CoreClient -> path", path);
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Map<String, Object> responseMap;
                try {
                    BasicResponseHandler response = new BasicResponseHandler();
                    String responseString = client.execute(get, response);
                    Log.d(TAG, "responseString = " + responseString);
                    responseMap = gson.fromJson(responseString, HashMap.class);
                    onSuccess.run(responseMap);
                } catch (ClientProtocolException e) {
                    Log.e(TAG, e.getMessage());
                    onError.run(e.getMessage());

                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    onError.run(e.getMessage());
                }
                return null;
            }
        };
        task.execute();
    }

    @Override
    public void doPostRequest(String path, Map<String, Object> data, final IDataCallback onSuccess, final IErrorCallback onError) {

        final HttpPost post = new HttpPost(hostName + path);
        post.setEntity(toJson(data));
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        Log.d(TAG, hostName);
        Log.d(TAG, path);
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Map<String, Object> responseMap;
                try {
                    BasicResponseHandler response = new BasicResponseHandler();
                    responseMap = gson.fromJson(client.execute(post, response), HashMap.class);
                    if (onSuccess != null) {
                        onSuccess.run(responseMap);
                    }
                } catch (ClientProtocolException e) {
                    Log.e(TAG, e.getMessage());
                    if (onError != null) {
                        onError.run(e.getMessage());
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    if (onError != null) {
                        onError.run(e.getMessage());
                    }
                }
                return null;
            }
        };
        task.execute();
    }
}
