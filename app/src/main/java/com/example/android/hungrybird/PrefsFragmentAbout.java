package com.example.android.hungrybird;

import android.os.Bundle;
import android.preference.PreferenceFragment;

	/*___________________________________________________________________
	|
	| Class: PrefsFragmentAbout
	|__________________________________________________________________*/
	public class PrefsFragmentAbout extends PreferenceFragment
	{
	    final static String TAG = "PrefsFragmentAbout";
	    
		/*___________________________________________________________________
		|
		| Function: PrefsFragmentAbout (constructor) 
		|__________________________________________________________________*/
	    public PrefsFragmentAbout () 
	    {
	    }
	    
		/*___________________________________________________________________
		|
		| Function: onCreate 
		|__________________________________________________________________*/
		@Override
	    public void onCreate (Bundle savedInstanceState) 
	    {
	    	super.onCreate(savedInstanceState);   	
	    	// Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.prefs_fragment_about);
	    }
		
		/*___________________________________________________________________
		|
		| Function: onResume 
		|__________________________________________________________________*/
	    @Override
	    public void onResume() 
	    {
	    	super.onResume();
	    	

	    }
	}

