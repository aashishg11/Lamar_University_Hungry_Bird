package com.example.android.hungrybird;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*___________________________________________________________________
|
| Class: PrefsFragmentInterface
|__________________________________________________________________*/
public class PrefsFragmentInterface extends PreferenceFragment implements OnSharedPreferenceChangeListener
{
    final static String TAG = "PrefsFragmentInterface";
	//public static CheckBoxPreference checkBox;
	//SharedPreferences checkboxPref;
	/*___________________________________________________________________
	|
	| Function: PrefsFragmentInterface (constructor) 
	|__________________________________________________________________*/
    public PrefsFragmentInterface () 
    {
		//checkboxPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
        addPreferencesFromResource(R.xml.prefs_fragment_interface);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//View view  = inflater.inflate(R.xml.prefs_fragment_interface,container,false);
		return super.onCreateView(inflater, container, savedInstanceState);
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

		final CheckBoxPreference checkBox = (CheckBoxPreference) getPreferenceManager().findPreference("key_tts");
		checkBox.setDefaultValue(true);
		checkBox.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				SharedPreferences checkboxPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
				SharedPreferences.Editor editor = checkboxPref.edit();
				if(newValue.toString().equals("true")){
					Assets.ttsEnabled = true;
					editor.putBoolean("ttsEnabled",Assets.ttsEnabled);
					editor.commit();
				}else {
					Assets.ttsEnabled = false;
					editor.putBoolean("ttsEnabled",Assets.ttsEnabled);
					editor.commit();
				}
				return true;
			}
		});

    	// Set up a click handler
    	Preference prefs = getPreferenceScreen().findPreference("key_restart");
		//ImageView img = (ImageView)
		//prefs.setIcon(R.drawable.icon_restart);
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
							// go back to main activity
							Intent intent = new Intent(getActivity(), MainActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getActivity().startActivity(intent);

						}
					})
    				.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
					})
    				.setIcon(android.R.drawable.ic_dialog_alert)
    				.show();
    			return (true);
    		}
    	});

    }
    
    
/*    
    @Override
    protected void onDraw ()
    {
    	// change the icon
    	
    	// call the base class version to draw everything
    	super.onDraw();
    	// invalidate so it draws again
    	invalidate ();
    }
*/    
    
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
}
