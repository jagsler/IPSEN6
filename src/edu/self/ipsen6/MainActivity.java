package edu.self.ipsen6;

import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {

	private static final int NOTIFICATION_ID = 1;
	private static final String SENDER_ID = "502694206342";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this); // Mag weg bij de publish
        final String regId = GCMRegistrar.getRegistrationId(this);
        System.out.println("REGID: " + regId);
        if (regId.equals("")) {
          GCMRegistrar.register(this, SENDER_ID);
        } else {
          Log.v("JAJA", "Already registered");
        }
    }
    
    public void test(View v) {
    	String ns = Context.NOTIFICATION_SERVICE;
    	NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
    	
    	int icon = R.drawable.ic_launcher;
    	CharSequence tickerText = "Tickertext";
    	long when = System.currentTimeMillis();

    	Notification notification = new Notification(icon, tickerText, when);
    	
    	Context context = getApplicationContext();
    	CharSequence contentTitle = "Title";
    	CharSequence contentText = "Hier dan het begin van een hele lap tekst ofzo";
    	Intent notificationIntent = new Intent(this, ReadMessage.class);
    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

    	notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

    	mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
