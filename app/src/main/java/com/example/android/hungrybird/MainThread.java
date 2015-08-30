package com.example.android.hungrybird;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.SoundPool;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private SurfaceHolder holder;
    private Handler handler;        // required for running code in the UI thread
    private boolean isRunning = false;
    Context context;
    Paint paint;
    int touchx, touchy;    // x,y of touch event
    boolean touched;    // true if touch happened on food
    float foodButtonPressedTimer; // if this time is in the future then draw pressed
    private static final Object lock = new Object();
    SoundPool soundPool;
    int sound_egg, sound_eating, sound_happy, sound_hungry, sound_last_played;
    public boolean isHappyFirst, isHungryFirst, isEatingFirst;


    public MainThread(SurfaceHolder surfaceHolder, Context context) {
        holder = surfaceHolder;
        this.context = context;
        handler = new Handler();
        touched = false;
        foodButtonPressedTimer = 0;

        /*soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        sound_egg = soundPool.load(context, R.raw.egg_crack, 1);
        sound_eating = soundPool.load(context, R.raw.eating, 1);
        sound_happy = soundPool.load(context, R.raw.happy, 1);
        sound_hungry = soundPool.load(context, R.raw.hungry, 1);*/
    }

    public void setRunning(boolean b) {
        isRunning = b;    // no need to synchronize this since this is the only line of code to writes this variable
    }

    // Set the touch event x,y location and flag indicating a touch has happened
    public void setXY(int x, int y) {
        touchx = x;
        touchy = y;
        synchronized (lock) {
            this.touched = true;
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            // Lock the canvas before drawing
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                // Perform drawing operations on the canvas
                render(canvas);
                // After drawing, unlock the canvas and display it
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    // Loads graphics, etc. used in game
    private void loadData(Canvas canvas) {
        Bitmap bmp;
        int newWidth, newHeight;
        float scaleFactor;

        // Create a paint object for drawing vector graphics
        paint = new Paint();

        // Load food bar
        Assets.bmp_egg_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.egg_1);
        Assets.bmp_egg_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.egg_2);
        Assets.bmp_egg_3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.egg_3);
        Assets.bmp_egg_4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.egg_4);
        Assets.bmp_happy_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.happy_1);
        Assets.bmp_happy_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.happy_2);
        Assets.bmp_hungry_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.hungry_1);
        Assets.bmp_hungry_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.hungry_2);
        Assets.bmp_eating_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.eating_1);
        Assets.bmp_eating_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.eating_2);

        // Compute size of bitmap needed (suppose want width = 50% of screen width)
        newWidth = (int) (canvas.getWidth() * 0.65f);
        // What was the scaling factor to get to this?
        scaleFactor = (float) newWidth / Assets.bmp_egg_1.getWidth();
        // Compute the new height
        newHeight = (int) (Assets.bmp_egg_1.getHeight() * scaleFactor);
        // Scale it to a new size
        Assets.bmp_egg_1 = Bitmap.createScaledBitmap(Assets.bmp_egg_1, newWidth, newHeight, false);
        Assets.bmp_egg_2 = Bitmap.createScaledBitmap(Assets.bmp_egg_2, newWidth, newHeight, false);
        Assets.bmp_egg_3 = Bitmap.createScaledBitmap(Assets.bmp_egg_3, newWidth, newHeight, false);
        Assets.bmp_egg_4 = Bitmap.createScaledBitmap(Assets.bmp_egg_4, newWidth, newHeight, false);
        Assets.bmp_happy_1 = Bitmap.createScaledBitmap(Assets.bmp_happy_1, newWidth, newHeight, false);
        Assets.bmp_happy_2 = Bitmap.createScaledBitmap(Assets.bmp_happy_2, newWidth, newHeight, false);
        Assets.bmp_hungry_1 = Bitmap.createScaledBitmap(Assets.bmp_hungry_1, newWidth, newHeight, false);
        Assets.bmp_hungry_2 = Bitmap.createScaledBitmap(Assets.bmp_hungry_2, newWidth, newHeight, false);
        Assets.bmp_eating_1 = Bitmap.createScaledBitmap(Assets.bmp_eating_1, newWidth, newHeight, false);
        Assets.bmp_eating_2 = Bitmap.createScaledBitmap(Assets.bmp_eating_2, newWidth, newHeight, false);

        Assets.bmp_food = BitmapFactory.decodeResource(context.getResources(), R.drawable.food);
        Assets.bmp_food_pressed = BitmapFactory.decodeResource(context.getResources(), R.drawable.food_pressed);
        Assets.bmp_options = BitmapFactory.decodeResource(context.getResources(), R.drawable.options);

        // Compute size of bitmap needed (suppose want width = 33% of screen width)
        newWidth = (int) (canvas.getWidth() * 0.25f);
        // What was the scaling factor to get to this?
        //scaleFactor = (float)newWidth / Assets.bmp_food.getWidth();
        // Compute the new height
        //newHeight = (int)(Assets.bmp_food.getHeight() * scaleFactor);
        Assets.bmp_food = Bitmap.createScaledBitmap(Assets.bmp_food, newWidth, newWidth, false);
        Assets.bmp_food_pressed = Bitmap.createScaledBitmap(Assets.bmp_food_pressed, newWidth, newWidth, false);
        Assets.bmp_options = Bitmap.createScaledBitmap(Assets.bmp_options, newWidth, newWidth, false);

    }

    // Load specific background screen
    private void loadBackground(Canvas canvas, int resId) {
        // Load background
        Assets.bmp_background = BitmapFactory.decodeResource(context.getResources(), resId);
        // Scale it to fill entire canvas
        Assets.bmp_background = Bitmap.createScaledBitmap(Assets.bmp_background, canvas.getWidth(), canvas.getHeight(), false);
    }

    private void render(Canvas canvas) {
        int i, x, y;
        float fx, fy, curTime, curTime1;
        int curTimeForAnim;

        // Get the value of checkbox state from shared preference
        SharedPreferences checkboxPref = PreferenceManager.getDefaultSharedPreferences(context);
        Assets.ttsEnabled = checkboxPref.getBoolean("ttsEnabled", true);


        //curTime = System.nanoTime() / 1000000000f;
        switch (Assets.state) {
            case Start:

                if (Assets.startingFresh) {
                    Assets.state = Assets.GameState.SplashScreen;
                    Assets.startingFresh = false;
                    // Start a timer
                    Assets.gameTimer = System.nanoTime() / 1000000000f;
                    // Load data and other graphics needed by game
                    loadData(canvas);
                    // Load a special "getting ready screen"
                    loadBackground(canvas, R.drawable.splash);
                }
                break;
            case SplashScreen:
                // Draw the background screen
                canvas.drawBitmap(Assets.bmp_background, 0, 0, null);
                // Has 3 seconds elapsed?
                curTime = System.nanoTime() / 1000000000f;
                if (curTime - Assets.gameTimer >= 3) {
                    // Load game play background
                    loadBackground(canvas, R.drawable.background);
                    // Goto next state
                    if (Assets.creatureBorn == false) {
                        Assets.state = Assets.GameState.Egg;
                        // soundPool.autoPause();
                        //  sound_last_played = soundPool.play(sound_egg, 1, 1, 1, 0, 1);
                        // Start a timer
                        Assets.gameTimer = System.nanoTime() / 1000000000f;
                    } else {
                        // It would be better to restore to the correct state from preferences
                        Assets.state = Assets.GameState.Happy;
                        //   soundPool.autoPause();
                        // sound_last_played = soundPool.play(sound_happy, 1, 1, 1, 0, 1);
                    }
                }
                break;
            case Egg:

                MainActivity.eggSound.start();

                /*if(!Assets.creatureBorn)
                {
                    soundPool.autoPause();
                    soundPool.play(sound_egg,1,1,1,-1,1);
                Assets.creatureBorn = true;
                }*/
                // Draw the background screen
                canvas.drawBitmap(Assets.bmp_background, 0, 0, null);
                // Draw the egg creature
                fx = canvas.getWidth() / 2 - Assets.bmp_egg_1.getWidth() / 2;
                fy = canvas.getHeight() / 2 - Assets.bmp_egg_1.getHeight() / 2;
                //canvas.drawBitmap(Assets.bmp_egg_1, fx, fy, null);

                /*curTime1 = System.nanoTime() / 10000000f;
                curTimeForAnim = (int) curTime1;
                curTimeForAnim %= 100;
                Log.d("Main thread", "time = " + curTimeForAnim);
                if (curTimeForAnim < 25 || (curTimeForAnim > 50 && curTimeForAnim < 76))
                    canvas.drawBitmap(Assets.bmp_egg_1, fx, fy, null);
                else
                    canvas.drawBitmap(Assets.bmp_egg_2, fx, fy, null);*/
                curTime = System.nanoTime() / 1000000000f;
                if (curTime - Assets.gameTimer <= 2)
                    canvas.drawBitmap(Assets.bmp_egg_1, fx, fy, null);
                if (curTime - Assets.gameTimer > 2 && curTime - Assets.gameTimer <= 5)
                    canvas.drawBitmap(Assets.bmp_egg_2, fx, fy, null);
                if (curTime - Assets.gameTimer > 5 && curTime - Assets.gameTimer <= 8)
                    canvas.drawBitmap(Assets.bmp_egg_3, fx, fy, null);
                if (curTime - Assets.gameTimer > 8 && curTime - Assets.gameTimer <= 10)
                    canvas.drawBitmap(Assets.bmp_egg_4, fx, fy, null);

                isHappyFirst = true;


                // Draw the options button
                canvas.drawBitmap(Assets.bmp_options, canvas.getWidth() * 0.1f, 0, null);
                // Wait for 10 seconds
                //curTime = System.nanoTime() / 1000000000f;
                MainActivity.eggSound.pause();
                if (curTime - Assets.gameTimer >= 10) {
                    // Goto next state
                    Assets.state = Assets.GameState.Happy;
                    // Set variable indicate creature born
                    Assets.creatureBorn = true;
                    // creature will be hungry in 20 seconds
                    Assets.timeWhenHungry = System.nanoTime() / 1000000000f + 20;
                    //  soundPool.autoPause();
                    // sound_last_played = soundPool.play(sound_happy, 1, 1, 1, 0, 1);
                }
                break;
            case Happy:


                if (Assets.ttsEnabled) {
                    if (isHappyFirst) {
                        MainActivity.tts.speak("I am happy. Thankyou for feeding me", false);
                        isHappyFirst = false;
                    }

                } else {
                    MainActivity.happySound.start();
                }

                // Draw the background screen
                canvas.drawBitmap(Assets.bmp_background, 0, 0, null);
                // Draw the happy creature
                fx = canvas.getWidth() / 2 - Assets.bmp_happy_1.getWidth() / 2;
                fy = canvas.getHeight() / 2 - Assets.bmp_happy_1.getHeight() / 2;
                //canvas.drawBitmap(Assets.bmp_happy, fx, fy, null);
                // Draw the options button
                canvas.drawBitmap(Assets.bmp_options, canvas.getWidth() * 0.1f, 0, null);
                // Draw food button
                curTime1 = System.nanoTime() / 10000000f;
                curTimeForAnim = (int) curTime1;
                curTimeForAnim %= 100;
                Log.d("Main thread", "time = " + curTimeForAnim);
                if (curTimeForAnim < 25 || (curTimeForAnim > 50 && curTimeForAnim < 76))
                    canvas.drawBitmap(Assets.bmp_happy_1, fx, fy, null);
                else
                    canvas.drawBitmap(Assets.bmp_happy_2, fx, fy, null);


                isEatingFirst = true;


                curTime = System.nanoTime() / 1000000000f;
                if (foodButtonPressedTimer > curTime)
                    canvas.drawBitmap(Assets.bmp_food_pressed, canvas.getWidth() * 0.66f, 0, null);
                else {
                    canvas.drawBitmap(Assets.bmp_food, canvas.getWidth() * 0.66f, 0, null);
                }
                // Draw options button
                // ADD CODE HERE
                // Check if happy timer has expired
                curTime = System.nanoTime() / 1000000000f;


                MainActivity.happySound.pause();
                if (curTime >= Assets.timeWhenHungry) {
                    // Goto hungry state
                    Assets.state = Assets.GameState.Hungry;
                    isHungryFirst = true;
                    // soundPool.autoPause();
                    //sound_last_played = soundPool.play(sound_hungry, 1, 1, 1, 0, 1);
                }

                break;
            case Hungry:

                // Draw the background screen
                //if (PrefsFragmentInterface.checkBox.isChecked()) {
                    if(Assets.ttsEnabled) {
                    if (isHungryFirst) {
                        MainActivity.tts.speak("I am hungry. Please feed me", false);
                        isHungryFirst = false;
                    }
                } else {
                    MainActivity.hungrySound.start();
                }
                canvas.drawBitmap(Assets.bmp_background, 0, 0, null);
                // Draw the hungry creature
                fx = canvas.getWidth() / 2 - Assets.bmp_hungry_1.getWidth() / 2;
                fy = canvas.getHeight() / 2 - Assets.bmp_hungry_1.getHeight() / 2;
                //canvas.drawBitmap(Assets.bmp_hungry_1, fx, fy, null);

                curTime1 = System.nanoTime() / 10000000f;
                curTimeForAnim = (int) curTime1;
                curTimeForAnim %= 100;
                Log.d("Main thread", "time = " + curTimeForAnim);
                if (curTimeForAnim < 25 || (curTimeForAnim > 50 && curTimeForAnim < 76))
                    canvas.drawBitmap(Assets.bmp_hungry_1, fx, fy, null);
                else
                    canvas.drawBitmap(Assets.bmp_hungry_2, fx, fy, null);

                //MainActivity.tts.speak("I am hungry",true);

                isEatingFirst = true;

                // Draw the options button
                canvas.drawBitmap(Assets.bmp_options, canvas.getWidth() * 0.1f, 0, null);
                // Draw food button
                curTime = System.nanoTime() / 1000000000f;
                if (foodButtonPressedTimer > curTime)
                    canvas.drawBitmap(Assets.bmp_food_pressed, canvas.getWidth() * 0.66f, 0, null);
                else
                    canvas.drawBitmap(Assets.bmp_food, canvas.getWidth() * 0.66f, 0, null);
                // Draw options button
                // ADD CODE HERE
                break;
            case Eating:


                //if (PrefsFragmentInterface.checkBox.isChecked()) {
                    if(Assets.ttsEnabled) {
                    if (isEatingFirst) {
                        MainActivity.tts.speak("I am eating. Please wait", false);
                        isEatingFirst = false;
                    }
                } else {
                    MainActivity.eatingSound.start();
                }
                // Draw the background screen
                canvas.drawBitmap(Assets.bmp_background, 0, 0, null);
                // Draw the eating creature
                fx = canvas.getWidth() / 2 - Assets.bmp_eating_1.getWidth() / 2;
                fy = canvas.getHeight() / 2 - Assets.bmp_eating_1.getHeight() / 2;
                //canvas.drawBitmap(Assets.bmp_eating, fx, fy, null);


                curTime1 = System.nanoTime() / 10000000f;
                curTimeForAnim = (int) curTime1;
                curTimeForAnim %= 100;
                Log.d("Main thread", "time = " + curTimeForAnim);
                if (curTimeForAnim < 25 || (curTimeForAnim > 50 && curTimeForAnim < 76))
                    canvas.drawBitmap(Assets.bmp_eating_1, fx, fy, null);
                else
                    canvas.drawBitmap(Assets.bmp_eating_2, fx, fy, null);

                // Draw the options button
                canvas.drawBitmap(Assets.bmp_options, canvas.getWidth() * 0.1f, 0, null);
                // Draw food button
                curTime = System.nanoTime() / 1000000000f;
                if (foodButtonPressedTimer > curTime)
                    canvas.drawBitmap(Assets.bmp_food_pressed, canvas.getWidth() * 0.66f, 0, null);
                // Draw options button
                // ADD CODE HERE
                // Check if happy timer has expired
                curTime = System.nanoTime() / 1000000000f;
                // Wait for 5 seconds

                isHappyFirst = true;

                MainActivity.eatingSound.pause();
                curTime = System.nanoTime() / 1000000000f;
                if (curTime - Assets.gameTimer >= 5) {
                    Assets.state = Assets.GameState.Happy;
                    //soundPool.autoPause();
                    //sound_last_played = soundPool.play(sound_happy, 1, 1, 1, 0, 1);
                }
                break;
        } // switch

        // Process a touch
        if (touched) {
            // Set touch flag to false since we are processing this touch now
            touched = false;

            // See if this touch was on the food button
            boolean foodTouched = FoodTouched(canvas, touchx, touchy);
            if (foodTouched) {
                // Set the timer to draw button pressed
                foodButtonPressedTimer = System.nanoTime() / 1000000000f + 0.5f;
                // creature will be hungry in 20 seconds
                if (Assets.timeWhenHungry + 20 < Assets.gameTimer + 120)
                    Assets.timeWhenHungry = System.nanoTime() / 1000000000f + 20f;
                // Go into eating state
                Assets.state = Assets.GameState.Eating;
                // Start a timer
                Assets.gameTimer = System.nanoTime() / 1000000000f;
                //soundPool.autoPause();
                //sound_last_played = soundPool.play(sound_eating, 1, 1, 1, 0, 1);
            }

            boolean optionsTouched = OptionsTouched(canvas, touchx, touchy);
            // See if this touch was on the options button
            if (optionsTouched) {
                handler.post(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(context, PrefsActivity.class);
                        context.startActivity(intent);
                        //Toast.makeText(context, "Options was clicked", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }


    // Process touch to see if the food button was clicked - return true if bug killed
    public boolean FoodTouched(Canvas canvas, int touchx, int touchy) {
        boolean touchedFood = false;
        // x,y is the center of the food button on screen
        float x = canvas.getWidth() * 0.66f + Assets.bmp_food.getWidth() / 2;
        float y = 0 + Assets.bmp_food.getHeight() / 2;
        // Make sure state is happy or hungry
        if (Assets.state == Assets.GameState.Happy ||
                Assets.state == Assets.GameState.Hungry) {
            // Compute distance between touch and center of bug
            float dis = (float) (Math.sqrt((touchx - x) * (touchx - x) + (touchy - y) * (touchy - y)));
            // Is this inside the button?
            if (dis <= Assets.bmp_food.getWidth() / 2)
                touchedFood = true;
        }
        return (touchedFood);
    }


    public boolean OptionsTouched(Canvas canvas, int touchx, int touchy) {
        boolean touchedOptions = false;
        // x,y is the center of the food button on screen
        float x1 = canvas.getWidth() * 0.1f;
        float y1 = 0;
        float x2 = canvas.getWidth() * 0.1f + Assets.bmp_options.getWidth();
        float y2 = 0 + Assets.bmp_food.getHeight();

        // Is this inside the button?
        if (touchx > x1 && touchx < x2 && touchy > y1 && touchy < y2)
            touchedOptions = true;

        return (touchedOptions);
    }

}
