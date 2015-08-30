package com.example.android.hungrybird;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView {
	private SurfaceHolder holder = null;
	Context context;
	private MainThread t = null;
	
	// Constructor 
	public MainView (Context context) {
		super(context);
		// Retrieve the SurfaceHolder instance associated with this SurfaceView. 
		holder = getHolder();
		
		// Initialize variables
		this.context = context;
		Assets.state = Assets.GameState.Start;
				
		// Load the sound effects
	    //Assets.soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		//Assets.sound_getready = Assets.soundPool.load(context, R.raw.getready, 1);
		//Assets.sound_squish = Assets.soundPool.load(context, R.raw.squish, 1);
		//Assets.sound_thump = Assets.soundPool.load(context, R.raw.thump, 1);
	}
	
	public void pause ()
	{
		t.setRunning(false);
		while (true) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		t = null;
	}
	
	public void resume () 
	{
		t = new MainThread (holder, context);
		t.setRunning(true);
		t.start();
		setFocusable(true); // make sure we get events
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		float x, y;
		int action = event.getAction();
		x = event.getX();
		y = event.getY();
//		if (action==MotionEvent.ACTION_MOVE) {
//		}
//		if (action==MotionEvent.ACTION_DOWN){
//		}
		if (action == MotionEvent.ACTION_UP) {
			t.setXY ((int)x, (int)y);
//			Log.i("ProjectLogging", "Touch at: " + x + "," + y);
		}		
		return true; // to indicate we have handled this event
	}
}
