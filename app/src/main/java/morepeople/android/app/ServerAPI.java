package morepeople.android.app;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
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

/**
 * Created by schreon on 3/14/14.
 */
public class ServerAPI implements IServerAPI {

    /** the host name */
    private String hostName;

    /** the http client */
    private HttpClient client;

    /** new gson */
    private Gson gson = new Gson();

    private static ServerAPI instance = null;
    public ServerAPI() {
        // TODO: load this from config
        ApplicationInfo ai = null;
        try {
            ai = MainApplication.getInstance().getPackageManager().getApplicationInfo(MainApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        hostName = (String) ai.metaData.get("morepeople.android.app.HOSTNAME");
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
    private void postCallback(DataCallback callback, Map<String, Object> data) {
        Looper.prepare();
        final DataCallback fCallback = callback;
        final Map<String, Object> fData = data;
        final Handler mHandler = new Handler();
        final Runnable mCallback = new Runnable() {
            public void run() {
                fCallback.run(fData);
            }
        };
        mHandler.post(mCallback);
        Looper.loop();
    }

    private void getRequest(String path, HashMap<String, String> data, DataCallback onSuccess, DataCallback onError) {

        final DataCallback fOnSuccess = onSuccess;
        final DataCallback fOnError = onError;

        // Url rewriting \m/
        String url = hostName;
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
        Log.d("ServerAPI -> url", url);
        Log.d("ServerAPI -> path", path);
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Map<String, Object> responseMap;
                try {
                    BasicResponseHandler response = new BasicResponseHandler();
                    responseMap = gson.fromJson(client.execute(get, response), HashMap.class);
                    postCallback(fOnSuccess, responseMap);
                } catch (ClientProtocolException e) {
                    Log.e("DataCallback", e.getMessage());
                    responseMap = new HashMap<String, Object>();
                    responseMap.put("ERROR", e.getMessage());
                    postCallback(fOnError, responseMap);

                } catch (IOException e) {
                    Log.e("DataCallback", e.getMessage());
                    responseMap = new HashMap<String, Object>();
                    responseMap.put("ERROR", e.getMessage());
                    postCallback(fOnError, responseMap);
                }
                return null;
            }
        };
        task.execute();
    }

    private void postRequest(String path, HashMap<String, Object> data, DataCallback onSuccess, DataCallback onError) {

        final DataCallback fOnSuccess = onSuccess;
        final DataCallback fOnError = onError;

        final HttpPost post = new HttpPost(hostName + path);
        post.setEntity(toJson(data));
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        Log.d("ServerAPI -> hostName", hostName);
        Log.d("ServerAPI -> path", path);
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Map<String, Object> responseMap;
                try {
                    BasicResponseHandler response = new BasicResponseHandler();
                    responseMap = gson.fromJson(client.execute(post, response), HashMap.class);
                    postCallback(fOnSuccess, responseMap);
                } catch (ClientProtocolException e) {
                    Log.e("DataCallback", e.getMessage());
                    responseMap = new HashMap<String, Object>();
                    responseMap.put("ERROR", e.getMessage());
                    postCallback(fOnError, responseMap);

                } catch (IOException e) {
                    Log.e("DataCallback", e.getMessage());
                    responseMap = new HashMap<String, Object>();
                    responseMap.put("ERROR", e.getMessage());
                    postCallback(fOnError, responseMap);
                }
                return null;
            }
        };
        task.execute();
    }

    @Override
    public void loadState(DataCallback onSuccess, DataCallback onError) {
        final DataCallback fOnSuccess = onSuccess;
        final DataCallback fOnError = onError;
        HashMap<String, Object> userInfo = MainApplication.getUserInfo();
        for (String key : userInfo.keySet()) {
            Log.d("ServerAPI", "-> userInfo." + key + " = " + userInfo.get(key));
        }
        postRequest("/state", userInfo, onSuccess, onError);
    }

    @Override
    public void searchEnvironment(DataCallback onSuccess, DataCallback onError) {
        HashMap<String, String> rewrite = new HashMap<String, String>();
        HashMap<String, Object> userInfo = MainApplication.getUserInfo();
        HashMap<String, String> loc = (HashMap<String, String>) userInfo.get("LOC");
        rewrite.put("LON", loc.get("LONGITUDE"));
        rewrite.put("LAT", loc.get("LATITUDE"));
        rewrite.put("RAD", "1000");
        getRequest("/queue", rewrite, onSuccess, onError);
    }

    @Override
    public void queue(String matchTag, DataCallback onSuccess, DataCallback onError) {

    }

    @Override
    public void cancel(DataCallback onSuccess, DataCallback onError) {

    }

    @Override
    public void confirmCancel(DataCallback onSuccess, DataCallback onError) {

    }

    @Override
    public void accept(DataCallback onSuccess, DataCallback onError) {

    }

    @Override
    public void finish(DataCallback onSuccess, DataCallback onError) {

    }

    @Override
    public void evaluate(DataCallback onSuccess, DataCallback onErrork) {

    }
}
