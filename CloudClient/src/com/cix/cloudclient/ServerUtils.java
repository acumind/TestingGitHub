package com.cix.cloudclient;


import static com.cix.cloudclient.Utils.displayMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.cix.cloudclient.AppSrvResponse;


public final class ServerUtils {
	private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
	private static final String TAG = "ServerUtilities";
	private static String gcmRegId = null;
	public static String vritiId = null;

    /**
     * Register this account/device pair within the server.
     *
     */
    static void register(final Context context, final String regId, final String vid) {
    	
    	gcmRegId = regId;
    	vritiId = vid;
    	
    	Log.d(TAG, "Registering device with Application Server(regId = " + regId + ")");
        String serverUrl = Utils.SERVER_URL;
        
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);

        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Try count " + i + " to register");
            try {
                displayMessage(context, "Registering attempt " +  i + "  of max attempts " + MAX_ATTEMPTS);
                post(serverUrl);
                GCMRegistrar.setRegisteredOnServer(context, true);
                 Utils.displayMessage(context, "Registered on Application server");
                return;
            } catch (IOException e) {
                Log.d(TAG, "Registraion failed on attempt " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
         Utils.displayMessage(context, "Error in registration with application server");
    }

    /**
     * Unregister this account/device pair within the server.
     */
    static void unregister(final Context context, final String regId, final String vid) {
        Log.i(TAG, "unregistering device (regId = " + regId + ") with app server");
        //String serverUrl = Utils.SERVER_URL;
        //post(serverUrl);
		GCMRegistrar.setRegisteredOnServer(context, false);
		Utils.displayMessage(context, "Unregistred with Application Server");
    }

    private static void post(String serverUrl)
            throws IOException {   	
        
        URL url;
        try {
            url = new URL(serverUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + serverUrl);
        }
        /*StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        
        String body = bodyBuilder.toString();
        Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();*/
        
        //String jsonData = "{\"vritiId\":\"1473067\", \"gcmRegId\":\"" + gcmRegId + "\"}";
        String jsonData = "{\"vritiId\":\"" + vritiId + "\", \"gcmRegId\":\"" + gcmRegId + "\"}";        
        Log.d(TAG,"JSON = " + jsonData);
        
        byte[] jBytes = jsonData.getBytes();
        HttpURLConnection conn = null;
        try {
        	Log.d("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(jBytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            // Send request
            OutputStream out = conn.getOutputStream();
            out.write(jBytes);
            out.close();
            
            // handle response
            int status = conn.getResponseCode();
            Log.d(TAG, "App Server response code = " + status);
  
            
            Gson gson = new Gson();
            InputStream ins = conn.getInputStream();
            Reader insr = new InputStreamReader(ins);
            AppSrvResponse appRes = gson.fromJson(insr, AppSrvResponse.class);
            
            Log.d(TAG, "App Server Json = " + appRes.ResponseCode);
            
            if (status != 200 && status != 201) { 
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
}
