package com.supermanket.supermanket;

import java.util.HashMap;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.supermanket.utilities.DiscussArrayAdapter;

public class GcmBroadcastReceiver extends BroadcastReceiver {
	
	static final String TAG = "GCMDemo";
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	Context ctx;
	private DiscussArrayAdapter adapter;
	private ListView list;
	
	public static Activity currentActivity;
	public static final Object CURRENTACTIVITYLOCK = new Object();
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		ctx = context;
		String messageType = gcm.getMessageType(intent);
	
		if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
		} else if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) { 
		} else {
			synchronized(CURRENTACTIVITYLOCK) {
	    		if (currentActivity != null) {
	    			if ((currentActivity.getClass() == MessageDetail.class)
	    					&& (Integer.parseInt(intent.getStringExtra("contact_id")) == MessageDetail.getContact())) {
	    				adapter = MessageDetail.getAdapter();
	    				list = MessageDetail.getList();
	    				HashMap<String, String> message = new HashMap<String, String>();
	    				message.put("message", intent.getStringExtra("message"));
	    				message.put("who", "other");
	    				adapter.add(message);
	    				list.setSelection(MessageDetail.getCurrentPosition());
	    			} else if(currentActivity.getClass() == MessagesList.class) {
	    				Toast.makeText(ctx, "Has recibido un mensaje de " + intent.getStringExtra("sender_username"), Toast.LENGTH_LONG).show();
	    			} else {
	    				Toast.makeText(ctx, "Has recibido un mensaje de " + intent.getStringExtra("sender_username"), Toast.LENGTH_LONG).show();
	    			}
	    		} else {
	    			sendNotification("Te ha llegado un mensaje.");
	        	}
			}
			
		}
		setResultCode(Activity.RESULT_OK);
	}
	
	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		
		PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, MessagesList.class), 0);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("Supermanket")
		.setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);
		
		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

}
