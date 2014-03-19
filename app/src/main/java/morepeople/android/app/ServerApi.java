package morepeople.android.app;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
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

/**
 * Created by schreon on 3/14/14.
 */
public class ServerApi implements IServerApi {

    /** the host name */
    private String hostName;

    /** the http client */
    private HttpClient client;

    /** new gson */
    private Gson gson = new Gson();

    private static ServerApi instance = null;
    public ServerApi() {
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

    private void getRequest(String path, HashMap<String, String> data, IDataCallback onSuccess, IDataCallback onError) {

        final IDataCallback fOnSuccess = onSuccess;
        final IDataCallback fOnError = onError;

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
        Log.d("ServerApi -> url", url);
        Log.d("ServerApi -> path", path);
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Map<String, Object> responseMap;
                try {
                    BasicResponseHandler response = new BasicResponseHandler();
                    String responseString = client.execute(get, response);
                    Log.d("ServerApi", "responseString = "+responseString);
                    responseMap = gson.fromJson(responseString, HashMap.class);
                    fOnSuccess.run(responseMap);
                } catch (ClientProtocolException e) {
                    Log.e("IDataCallback", e.getMessage());
                    responseMap = new HashMap<String, Object>();
                    responseMap.put("ERROR", e.getMessage());
                    fOnError.run(responseMap);

                } catch (IOException e) {
                    Log.e("IDataCallback", e.getMessage());
                    responseMap = new HashMap<String, Object>();
                    responseMap.put("ERROR", e.getMessage());
                    fOnError.run(responseMap);
                }
                return null;
            }
        };
        task.execute();
    }

    private void postRequest(String path, HashMap<String, Object> data, IDataCallback onSuccess, IDataCallback onError) {

        final IDataCallback fOnSuccess = onSuccess;
        final IDataCallback fOnError = onError;

        final HttpPost post = new HttpPost(hostName + path);
        post.setEntity(toJson(data));
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        Log.d("ServerApi -> hostName", hostName);
        Log.d("ServerApi -> path", path);
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Map<String, Object> responseMap;
                try {
                    BasicResponseHandler response = new BasicResponseHandler();
                    responseMap = gson.fromJson(client.execute(post, response), HashMap.class);
                    fOnSuccess.run(responseMap);
                } catch (ClientProtocolException e) {
                    Log.e("IDataCallback", e.getMessage());
                    responseMap = new HashMap<String, Object>();
                    responseMap.put("ERROR", e.getMessage());
                    fOnError.run(responseMap);

                } catch (IOException e) {
                    Log.e("IDataCallback", e.getMessage());
                    responseMap = new HashMap<String, Object>();
                    responseMap.put("ERROR", e.getMessage());
                    fOnError.run(responseMap);
                }
                return null;
            }
        };
        task.execute();
    }

    @Override
    public void loadState(IDataCallback onSuccess, IDataCallback onError) {
        final IDataCallback fOnSuccess = onSuccess;
        final IDataCallback fOnError = onError;
        HashMap<String, Object> userInfo = MainApplication.getUserInfo();
        for (String key : userInfo.keySet()) {
            Log.d("ServerApi", "-> userInfo." + key + " = " + userInfo.get(key));
        }
        postRequest("/state", userInfo, onSuccess, onError);
    }

    @Override
    public void searchEnvironment(Location location, IDataCallback onSuccess, IDataCallback onError) {
        HashMap<String, String> rewrite = new HashMap<String, String>();
        rewrite.put("LON", String.valueOf(location.getLongitude()));
        rewrite.put("LAT", String.valueOf(location.getLatitude()));
        rewrite.put("RAD", "1000");
        getRequest("/queue", rewrite, onSuccess, onError);
    }

    @Override
    public void queue(String matchTag, IDataCallback onSuccess, IDataCallback onError) {

    }

    @Override
    public void cancel(IDataCallback onSuccess, IDataCallback onError) {

    }

    @Override
    public void confirmCancel(IDataCallback onSuccess, IDataCallback onError) {

    }

    @Override
    public void accept(IDataCallback onSuccess, IDataCallback onError) {

    }

    @Override
    public void finish(IDataCallback onSuccess, IDataCallback onError) {

    }

    @Override
    public void evaluate(IDataCallback onSuccess, IDataCallback onErrork) {

    }
}
