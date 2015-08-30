package com.example.android.hungrybird;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

public class BirdService extends MyIntentService {

	public static final String ACTION_START = "com.example.gator.START";
	public static final String ACTION_KILL_NOTIFY = "com.example.gator.KILL_NOTIFY";
	
	NotificationManager mNotifyManager;
	Notification mNotify = null;
	final int NOTIFICATION_ID = 1;
	
	public BirdService() {
		super("BirdService");
	}
	@Override
	public void onCreate ()
	{
		super.onCreate();
		Assets.serviceIsProcessing = false;
		// Get a handle to the notification manager
		mNotifyManager = (NotificationManager) getSystemService (NOTIFICATION_SERVICE);
		
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		
		if (action.equals(ACTION_START))
			processStart();
		else if (action.equals(ACTION_KILL_NOTIFY))
			processStopNotify();
		
	}
	void processStart () 
	{
		float curTime;
		
		if (Assets.serviceIsProcessing)
			return;
		
		Assets.serviceIsProcessing = true;
		do {
			curTime = System.nanoTime() / 1000000000f;
		} while (curTime < Assets.timeWhenHungry);
		
		// Prepare the pending Intent
		Intent intent = new Intent (this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this,  0,  intent,  
				     PendingIntent.FLAG_UPDATE_CURRENT);
		// Show the notification
		Notification n = new Notification.Builder(this)
			.setContentTitle("Hungry Bird")
			.setContentText("Feed me I am hungry !!!")
			.setSmallIcon(R.drawable.happy_2)
			.setContentIntent(pIntent)
			.setAutoCancel(true)
			.build();
		mNotifyManager.notify(NOTIFICATION_ID, n);
		
		Assets.serviceIsProcessing = false;
	}
	void processStopNotify() 
	{
		mNotifyManager.cancel(NOTIFICATION_ID);
	}
	@Override
	public void onDestroy ()
	{
		mNotifyManager = null;
		super.onDestroy();
	}
}
