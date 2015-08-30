package com.example.android.hungrybird;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;
 
/*___________________________________________________________________
|
| Class: PrefsActivity
|__________________________________________________________________*/
public class PrefsActivity extends PreferenceActivity
{
    // Tag for debug messages
    final static String TAG = "PrefsActivity";

	/*___________________________________________________________________
	|
	| Function: onCreate 
	|__________________________________________________________________*/
    @Override
    public void onCreate (Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);       
    }

	/*___________________________________________________________________
	|
	| Function: isValidFragment 
	|__________________________________________________________________*/
    @Override
    protected boolean isValidFragment (String fragmentName)
    {
    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
    		return true;
    	else if ((PrefsFragmentAbout.class.getName().equals(fragmentName)) ||
		PrefsFragmentInterface.class.getName().equals(fragmentName))
    		return true;
   
    	return false;
    }
    
	/*___________________________________________________________________
	|
	| Function: onBuildHeaders 
	|__________________________________________________________________*/
    @Override
    public void onBuildHeaders (List<Header> target) 
    {
    	// Use this to load an XML file containing references to multiple fragments (a multi-screen preferences screen)
    	loadHeadersFromResource(R.xml.prefs_headers, target);
    	
    	// Use this to load an XML file containing a single preferences screen
    	//getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragmentSettings()).commit();
    }  
}
