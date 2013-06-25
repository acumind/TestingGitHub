package com.cix.cloudclient;

import static com.cix.cloudclient.Utils.displayMessage;
import static com.cix.cloudclient.Utils.SENDER_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

public class GCMIntentService extends com.google.android.gcm.GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	private static int mId = 0;
	

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.d(TAG, "Received error: " + errorId);
        displayMessage(context, "Error with id " + errorId + "received");
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {

        Log.d(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, "Recoverable Error with id " + errorId + "received");
        return super.onRecoverableError(context, errorId);
    }

	@Override
	protected void onMessage(Context ctx, Intent intent) {
        Log.d(TAG, "Message Received");
        String message = intent.getExtras().getString("message");
        displayMessage(ctx, message);
        generateNotification(ctx, message);
	}

	@Override
	protected void onRegistered(Context ctx, String regId) {
        Log.i(TAG, "Device registered with Id = " + regId);
        displayMessage(ctx, "Device registered with GCM");
        ServerUtils.register(ctx, regId,ServerUtils.vritiId);
	}

	@Override
	protected void onUnregistered(Context ctx, String regId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(ctx, "Device Unregisterd with GCM");
        ServerUtils.unregister(ctx, regId,ServerUtils.vritiId);
	}
	
	
	
	private static void generateNotification(Context ctx, String message) {
		
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        
        String title = ctx.getString(R.string.app_name);
        
        Intent notificationIntent = new Intent(ctx, CloudClientMainActivity.class);
        Bundle extras = new Bundle();
        extras.putString("noti_title", "Vriti Notification");
        extras.putString("noti_detail",message);
        notificationIntent.putExtras(extras);
        
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(ctx, 0, notificationIntent, 0);
        notification.setLatestEventInfo(ctx, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);      

        
/*       //For target API 16
  
  		NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(ctx.getString(R.string.notification_title_vriti))
                .setContentText(message.toString());

        Intent notificationIntent = new Intent(ctx, CloudClientMainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(CloudClientMainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(notificationPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId++, mBuilder.build());        
        */
        
    }

	
	

}
