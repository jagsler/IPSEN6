package edu.self.ipsen6;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Set;
import java.util.Scanner;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Vibrator;

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
		for (String s : set) {
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
		notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE
				| Notification.FLAG_AUTO_CANCEL;

		CharSequence contentTitle = title;
		CharSequence contentText = bericht;

		Intent notificationIntent = new Intent(this, ReadMessage.class);
		notificationIntent.putExtra("TITEL", title);
		notificationIntent.putExtra("BERICHT", bericht);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// Get instance of Vibrator from current Context
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// This example will cause the phone to vibrate "SOS" in Morse Code
		// In Morse Code, "s" = "dot-dot-dot", "o" = "dash-dash-dash"
		// There are pauses to separate dots/dashes, letters, and words
		// The following numbers represent millisecond lengths
		int dot = 200; // Length of a Morse Code "dot" in milliseconds
		int dash = 500; // Length of a Morse Code "dash" in milliseconds
		int short_gap = 200; // Length of Gap Between dots/dashes
		int medium_gap = 500; // Length of Gap Between Letters
		int long_gap = 1000; // Length of Gap Between Words
		long[] pattern = { 0, // Start immediately
				dot, short_gap, dot, short_gap, dot, // s
				medium_gap, dash, short_gap, dash, short_gap, dash, // o
				medium_gap, dot, short_gap, dot, short_gap, dot, // s
				long_gap };

		// Only perform this pattern one time (-1 means "do not repeat")
		v.vibrate(pattern, 12);

		mNotificationManager.notify(NOTIFICATION_ID + (int) System.nanoTime(),
				notification);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		System.out.println("id is dus" + regId);
		sendJson(regId);
	}

	public void sendJson(final String regId) {
		System.out.println("GOING INTO THA JSONNNN");
		Thread t = new Thread() {
			public void run() {
				Looper.prepare();
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						10000);
				HttpResponse response;
				JSONObject json = new JSONObject();
				try {
					System.out.println("uitproberen die handel");
					HttpPost post = new HttpPost(
							"http://notifications.appplus.net/v1/register/post?format=json");
					
					// Vul het json object
					json.put("device_id", regId);
					json.put("registration_id", regId);
					json.put("email", "aa@gmail.com");
					json.put("type", 2);
					StringEntity se = new StringEntity(json.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					post.setEntity(se);
					
					// Handel rammen
					response = client.execute(post);

					// Response checken
					if (response != null) {
						System.out.println("Repsonse is niet null");
						
						// Vraag de inputstream op
						// Zonder object geeft dat wel inhoud, met een gevuld object gebeurt er niets
						InputStream in = response.getEntity().getContent();
						String s = new Scanner(in).useDelimiter("\\A").next();
						
						System.out.println("inhoud: " + s);
					} else {
						System.out.println("response is null");
					}
					System.out.println("Made it");
				} catch (Exception e) {
					e.printStackTrace();

				}
				Looper.loop();
			}
		};
		t.start();
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
