/*___________________________________________________________________
|
| File: PrefsFragmentSettings.java
|
| Functions:	PrefsFragmentSettings (constructor)
|				onCreate
|				onResume
|
| Copyright 2015 Abonvita Software LLC.
|__________________________________________________________________*/
package com.example.android.hungrybird;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
 
/*___________________________________________________________________
|
| Class: PrefsFragmentSettings
|__________________________________________________________________*/
public class PrefsFragmentSettings extends PreferenceFragment 
    implements OnSharedPreferenceChangeListener {
    final static String TAG = "PrefsFragmentSettings";
    
	/*___________________________________________________________________
	|
	| Function: PrefsFragmentSettings (constructor) 
	|__________________________________________________________________*/
    public PrefsFragmentSettings () 
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
        addPreferencesFromResource(R.xml.prefs_fragment_settings);
    }
	
	/*___________________________________________________________________
	|
	| Function: onResume 
	|__________________________________________________________________*/
    @Override
    public void onResume() 
    {
    	super.onResume();
    	
    	getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    	

    	// Set up a click handler
		Preference prefs;
    	prefs = getPreferenceScreen().findPreference("key_restart");
    	
    	prefs.setOnPreferenceClickListener(new OnPreferenceClickListener() {
    		public boolean onPreferenceClick (Preference preference) {
    			new AlertDialog.Builder(getActivity())
    				.setTitle("Restart Game?")
    				.setMessage("Are you sure?")
    				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog, int which) {
    						// exit the preferences screen
							Assets.creatureBorn = false;
							Assets.gameTimer = System.nanoTime() / 1000000000f;
    						Assets.state = Assets.GameState.Egg; // set state of game, etc.
    						getActivity().finish();
    					}
    				})
    				.setNegativeButton("No", new DialogInterface.OnClickListener() {
    					public void onClick (DialogInterface dialog, int which) {
    						// do nothing
    					}
    				})
    				.setIcon(android.R.drawable.ic_dialog_alert)
    				.show();
    			return (true);
    		}
    	});
    	
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
}
