package com.example.android.hungrybird;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    MainView v;

    public static MediaPlayer happySound, hungrySound, eatingSound, eggSound;
    public static AndroidTTS tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Disable the title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Make full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Set the variable indicating if we are starting fresh or resuming
        if (savedInstanceState == null)
            Assets.startingFresh = true;
        else
            Assets.startingFresh = false;
        // Retrieve the value of creatureBorn from default preferences
        SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(this);
        Assets.creatureBorn = sprefs.getBoolean("creatureBorn", false);
        //Assets.ttsEnabled = sprefs.getBoolean("ttsEnabled", true);
        //Assets.ttsEnabled = true;
        //SharedPreferences.Editor editor = sprefs.edit();
        //editor.putBoolean("ttsEnabled",Assets.ttsEnabled);
        //editor.commit();


        // Start the view
        v = new MainView(this);
        setContentView(v);

        // Load the sounds
        happySound = MediaPlayer.create(this, R.raw.happy);
        hungrySound = MediaPlayer.create(this, R.raw.hungry);
        eatingSound = MediaPlayer.create(this, R.raw.eating);
        eggSound = MediaPlayer.create(this, R.raw.egg_crack);

        // Set the TTS engine
        tts = new AndroidTTS(this);


    }

    @Override
    protected void onPause() {
        // Write out the value of creatureBorn to default preferences
        SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = sprefs.edit();
        editor.putBoolean("creatureBorn", Assets.creatureBorn);
        //editor.putBoolean("ttsEnabled",Assets.ttsEnabled);
        editor.commit();


        if (!isFinishing()) {
            Intent intent = new Intent(this, BirdService.class);
            intent.setAction(BirdService.ACTION_START);
            startService(intent);
        }
        super.onPause();
        v.pause();
    }

    @Override
    protected void onResume() {
        // Stop the notification
        Intent intent = new Intent(this, BirdService.class);
        intent.setAction(BirdService.ACTION_KILL_NOTIFY);
        startService(intent);

        SharedPreferences checkboxPref = PreferenceManager.getDefaultSharedPreferences(this);
        Assets.ttsEnabled = checkboxPref.getBoolean("ttsEnabled", true);

        if (Assets.state.equals(Assets.GameState.Happy)) {
            if (Assets.ttsEnabled) {
                MainActivity.tts.speak("I am happy. Thankyou for feeding me", false);
            } else {
                MainActivity.happySound.start();
            }
        }

        if (Assets.state.equals(Assets.GameState.Hungry)) {
            if (Assets.ttsEnabled) {
                MainActivity.tts.speak("I am hungry. Please feed me", false);
            } else {
                MainActivity.hungrySound.start();
            }
        }

        super.onResume();
        v.resume();
    }
}

