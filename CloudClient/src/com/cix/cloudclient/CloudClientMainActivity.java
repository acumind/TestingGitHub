package com.cix.cloudclient;


import com.cix.cloudclient.Utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class CloudClientMainActivity extends Activity {


	private static final String TAG = "CloudClientMainActivity";
	String regId;	
	TextView msgTV;
	AsyncTask<Void, Void, Void> mRegisterTask;

	AlertDialogManager alert = new AlertDialogManager();
	
	
	Network net;

	public static String action;
	public static String vritiId;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cloud_client_main);
				
		net = new Network(getApplicationContext());

		if (!net.isNetworkConnected()) {
			alert.showAlertDialog(this, "Network Connection Error", "Please connect to Network", false);
			return;
		}

		Intent i = getIntent();
		action = i.getStringExtra("action");
		vritiId = i.getStringExtra("vid");
		ServerUtils.vritiId = vritiId;
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		msgTV = (TextView) findViewById(R.id.msgTV);				
		
		if( action.equalsIgnoreCase("reg")) {

			registerReceiver(mHandleMessageReceiver, new IntentFilter(Utils.DISPLAY_MSG_ACTION));		
			
			regId = GCMRegistrar.getRegistrationId(this);
			Log.v(TAG, "Registration Id = " + regId);
			
			if (regId.equals("")) {
				Log.v(TAG, "Registering with GCM server.");			
				GCMRegistrar.register(this, Utils.SENDER_ID);
			} else {
	
				if (GCMRegistrar.isRegisteredOnServer(this)) {
					Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
				} else {
	
					final Context context = this;
					mRegisterTask = new AsyncTask<Void, Void, Void>() {
	
						@Override
						protected Void doInBackground(Void... params) {
							ServerUtils.register(context,regId,vritiId);
							return null;
						}
	
						@Override
						protected void onPostExecute(Void result) {
							mRegisterTask = null;
						}
	
					};
					mRegisterTask.execute(null, null, null);
				}
			}
		}
		else if( action.equalsIgnoreCase("unreg")) {
			
			ServerUtils.unregister(getApplicationContext(),regId,vritiId);
		}
	}				
		

	/*
	 * Receiving push messages from GCM
	 * 
	 */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(Utils.EXTRA_MSG);
			// Waking up device, if sleeping
			WakeLocker.acquire(getApplicationContext());
			
			msgTV.append(newMessage + "\n");			
			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			
			// Releasing wake lock
			WakeLocker.release();
		}
	};
	
	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			//GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("Error in unregistering", "> " + e.getMessage());
		}
		super.onDestroy();
	}	
	


}
