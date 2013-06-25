package com.cix.cloudclient;

import android.content.Context;
import android.content.Intent;

public class Utils {

		//static final String SERVER_URL = "http://d.accelerateaipmt.lms.vriti.com/gcm_register"; 
		static final String SERVER_URL = "http://d.accelerateaipmt.lmsvriti.com/rest/VritiRestService/mapRegIdToUser/";
	    static final String SENDER_ID = "964103945096"; 
	    static final String TAG = "CloudClient GCM Client";
	    static final String DISPLAY_MSG_ACTION = "com.cix.cloudclient.DISPLAY_MSG";
	    static final String EXTRA_MSG = "message";

	    static void displayMessage(Context context, String message) {
	        Intent intent = new Intent(DISPLAY_MSG_ACTION);
	        intent.putExtra(EXTRA_MSG, message);
	        context.sendBroadcast(intent);
	    }
}
