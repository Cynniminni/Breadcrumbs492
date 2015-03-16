package com.tumblr.breadcrumbs492.testapplication;

/**
 * Created by scott on 3/8/2015.
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.tumblr.breadcrumbs492.testapplication.LoginActivity.MyRequestReceiver;
import com.tumblr.breadcrumbs492.testapplication.ProfileActivity.MyRequestReceiver1;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class JSONRequest extends IntentService{

    private String inMessage;
    private String receivedIntent;

    public static final String IN_MSG = "requestType";
    public static final String OUT_MSG = "outputMessage";

    public JSONRequest() {
        super("JSONRequest");
    }

    @Override
    protected void onHandleIntent(Intent intent) {



        //Get Intent extras that were passed
        inMessage = intent.getStringExtra(IN_MSG);
        if(inMessage.trim().equalsIgnoreCase("login")){
            this.receivedIntent = intent.getStringExtra("intent");
            String queryID = intent.getStringExtra("queryID");
            String jsonObject = intent.getStringExtra("jsonObject");
            getUserInfo(queryID, jsonObject);
        }
        else if(inMessage.trim().equalsIgnoreCase("getProfile")){
            String queryID = intent.getStringExtra("queryID");
            String jsonObject = intent.getStringExtra("jsonObject");
            getUserInfo(queryID, jsonObject);
        }

    }

    private void getUserInfo(String queryID, String jsonObject) {

        //prepare to make Http request
        String url = "http://54.153.38.154:8080/crumbs/CrumbsServer";

        //add name value pair for the country code
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("queryID",queryID));
        nameValuePairs.add(new BasicNameValuePair("jsonObject",jsonObject));
        String response = sendHttpRequest(url,nameValuePairs);

        //broadcast message that we have received the reponse
        //from the WEB Service
        Intent broadcastIntent = new Intent();
        broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        if(queryID.equals("login")) {
            broadcastIntent.setAction(MyRequestReceiver.PROCESS_RESPONSE);
            broadcastIntent.putExtra("intent", receivedIntent);
        }
        else if(queryID.equals("getProfile"))
            broadcastIntent.setAction(MyRequestReceiver1.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(IN_MSG, inMessage);
        broadcastIntent.putExtra(OUT_MSG, response);

        sendBroadcast(broadcastIntent);
    }

    private String sendHttpRequest(String url, List<NameValuePair> nameValuePairs) {

        int REGISTRATION_TIMEOUT = 15 * 1000;
        int WAIT_TIMEOUT = 60 * 1000;
        HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpResponse response;
        String content =  "";

        try {

            //http request parameters
            HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
            ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

            //http POST
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            //send the request and receive the repponse
            response = httpclient.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);

                content = out.toString();
                out.close();

            }

            else{
                //Closes the connection.
                Log.w("HTTP1:",statusLine.getReasonPhrase());
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (ClientProtocolException e) {
            Log.w("HTTP2:",e );
        } catch (IOException e) {
            Log.w("HTTP3:",e );
        }catch (Exception e) {
            Log.w("HTTP4:",e );
        }

        //send back the JSON response String
        return content;

    }


}