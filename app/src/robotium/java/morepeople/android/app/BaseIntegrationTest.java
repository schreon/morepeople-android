package morepeople.android.app;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by friedolin on 3/14/14.
 */
public class BaseIntegrationTest<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    private String url = "http://109.230.231.200";

    public BaseIntegrationTest(Class<T> activityClass) {
        super(activityClass);
    }

    public JSONObject doGetRequest(String url) {
        url = this.url + url;

        BasicResponseHandler handler = new BasicResponseHandler();
        HttpClient httpclient = new DefaultHttpClient();

        String str = null;
        try {
            HttpGet request = new HttpGet(url);
            str = httpclient.execute(request, handler);
        } catch (ClientProtocolException e) {
            Log.e("http", "http: ClientProtocolException in get request '"+url+"'");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("http", "http: IOException in get request '"+url+"'");
        }
        JSONObject jsonObject = null;
        if(str != null) {
            try {
                jsonObject = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    public void doPostRequest(String url) {
        doPostRequest(url, new JSONObject());
    }

    public JSONObject doPostRequest(String url,JSONObject obj) {
        url = this.url + url;

        BasicResponseHandler handler = new BasicResponseHandler();
        HttpParams myParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, 10000);
        HttpConnectionParams.setSoTimeout(myParams, 10000);
        HttpClient httpclient = new DefaultHttpClient(myParams);
        String json=obj.toString();

        String str = null;
        try {
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(json);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);

            str = httpclient.execute(httppost, handler);
        } catch (ClientProtocolException e) {
            Log.e("http", "http: ClientProtocolException in post request '"+url+"'");
        } catch (IOException e) {
            Log.e("http", "http: IOException in post request '"+url+"'");
        }
        JSONObject jsonObject = null;
        if(str != null) {
            try {
                jsonObject = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

}
