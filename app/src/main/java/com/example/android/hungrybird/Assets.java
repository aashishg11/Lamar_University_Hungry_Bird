package com.example.android.hungrybird;

import android.graphics.Bitmap;

public class Assets {
	
	static Bitmap bmp_background;
	static Bitmap bmp_egg_1;
	static Bitmap bmp_egg_2;
	static Bitmap bmp_egg_3;
	static Bitmap bmp_egg_4;
	static Bitmap bmp_happy_1;
	static Bitmap bmp_happy_2;
	static Bitmap bmp_hungry_1;
	static Bitmap bmp_hungry_2;
	static Bitmap bmp_eating_1;
	static Bitmap bmp_eating_2;
	static Bitmap bmp_options;
	static Bitmap bmp_food, bmp_food_pressed;
	
	static float gameTimer;	// in seconds
	
	// timestamp of when the gator will be hungry
	public static float timeWhenHungry;
	
	// True when the service is processing a wait timer
	public static volatile boolean serviceIsProcessing;
	
	// States of the Game Screen
	enum GameState {
		Start,	
		SplashScreen,
		Egg,
		Happy,
		Hungry,
		Eating	
	};
	public static GameState state;		
	
	public static boolean startingFresh;
	public static boolean creatureBorn;
	public static boolean ttsEnabled = true;
}
