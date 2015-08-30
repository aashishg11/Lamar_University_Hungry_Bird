package com.example.android.hungrybird;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

/*___________________________________________________________________
|
| Class: AndroidTTS
|
| Description: Manager for text to speech engine.
|__________________________________________________________________*/
public class AndroidTTS implements OnInitListener
{
	TextToSpeech tts;
	boolean initialized;
	
	/*___________________________________________________________________
	|
	| Function: AndroidTTS (constructor)
	|__________________________________________________________________*/
	public AndroidTTS (Context context) 
	{
		initialized = false;
		tts = new TextToSpeech (context, this);
	}
	
	/*___________________________________________________________________
	|
	| Function: onInit 
	|
	| Description: Called back OS when a TTS engine has finished initializing.
	|__________________________________________________________________*/
	@Override
	public void onInit (int status)
	{
		if (status == TextToSpeech.SUCCESS)
			initialized = true;
		else
			initialized = false;
	}

	/*___________________________________________________________________
	|
	| Function: setRate 
	|
	| Description: Sets the rate of the speaker.  For example:
	|				1 = normal
	|				2 = twice as fast
	|__________________________________________________________________*/
	public void setRate (float rate)
	{
		if (initialized) {
			tts.setSpeechRate (rate);
		}
	}
	
	/*___________________________________________________________________
	|
	| Function: setPitch 
	|
	| Description: Sets the pitch of the speaker.  For example:
	|				1 = normal
	|				<1 = lower tone
	|				>1 = higher tone
	|__________________________________________________________________*/
	public void setPitch (float pitch)
	{
		if (initialized) {
			tts.setPitch (pitch);
		}
	}
	
	/*___________________________________________________________________
	|
	| Function: speak 
	|
	| Description: Speaks a string of text.
	|__________________________________________________________________*/
	public void speak (String s, boolean flush_queue)
	{
		if (initialized) {
			if (flush_queue)
				tts.speak (s, TextToSpeech.QUEUE_FLUSH, null);
			else
				tts.speak (s, TextToSpeech.QUEUE_ADD, null);
		}
	}

	/*___________________________________________________________________
	|
	| Function: speakSilence
	|
	| Description: Plays silence for the specified amount of time.
	|__________________________________________________________________*/
	public void speakSilence (long durationMilliseconds, boolean flush_queue)
	{
		if (initialized) {
			if (flush_queue)
				tts.playSilence (durationMilliseconds, TextToSpeech.QUEUE_FLUSH, null);
			else
				tts.playSilence (durationMilliseconds, TextToSpeech.QUEUE_ADD, null);
		}
	}
	
	/*___________________________________________________________________
	|
	| Function: stop 
	|
	| Description: Stops speaking and discards any speak requests in the
	|	speak queue.
	|__________________________________________________________________*/
	public void stop ()
	{
		if (initialized) 
			tts.stop();
	}

	/*___________________________________________________________________
	|
	| Function: isSpeaking 
	|
	| Description: Returns true if speaking currently.
	|__________________________________________________________________*/
	public boolean isSpeaking ()
	{
		if (initialized) 
			return tts.isSpeaking();
		else
			return false;
	}

	/*___________________________________________________________________
	|
	| Function: free 
	|
	| Description: Stops the tts engine and releases any resources used.
	|__________________________________________________________________*/
	public void free ()
	{
		if (initialized) {
			tts.stop ();
			tts.shutdown ();
			initialized = false;
		}
	}
}
