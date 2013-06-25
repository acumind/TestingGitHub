package com.cix.cloudclient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

	   private Context ctx;
	   
	    public Network(Context context){
	        this.ctx = context;
	    }
	 
	    public boolean isNetworkConnected(){
	        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	          if (connectivity != null)
	          {
	              NetworkInfo[] info = connectivity.getAllNetworkInfo();
	              if (info != null)
	                  for (int i = 0; i < info.length; i++)
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                          return true;
	                      }
	 
	          }
	          return false;
	    }	
	
}
