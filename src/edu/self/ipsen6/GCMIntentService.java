package edu.self.ipsen6;

import java.util.Set;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final int NOTIFICATION_ID = 0x0102;
	
	@Override
	protected void onError(Context context, String errorId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("LIJP BERICHT");
		Set<String> set = intent.getExtras().keySet();
		for(String s : set) {
			System.out.println("inhoud: " + s);
		}
		
		String from = intent.getStringExtra("from");
		
		String bericht = intent.getStringExtra("body");
		String title = intent.getStringExtra("header");
		
		System.out.println("Bericht: " + bericht + ", FROM " + from);
		
		String ns = Context.NOTIFICATION_SERVICE;
    	NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
    	
    	int icon = R.drawable.ic_launcher;
    	long when = System.currentTimeMillis();

    	Notification notification = new Notification(icon, bericht, when);
    	
    	CharSequence contentTitle = title;
    	CharSequence contentText = bericht;
    	
    	Intent notificationIntent = new Intent(this, ReadMessage.class);
    	notificationIntent.putExtra("TITEL", title);
    	notificationIntent.putExtra("BERICHT", bericht);
    	
    	
    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

    	notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

    	mNotificationManager.notify(NOTIFICATION_ID, notification);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return false;
	}

}
