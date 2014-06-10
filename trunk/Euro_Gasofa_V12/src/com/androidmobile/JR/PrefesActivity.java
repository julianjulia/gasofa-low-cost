package com.androidmobile.JR;




import com.androidmobile.service.UpdaterService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.Window;
import android.webkit.WebView;

public class PrefesActivity extends PreferenceActivity implements OnPreferenceChangeListener,OnSharedPreferenceChangeListener {
	private static final String JAVASCRIPT = "javascript:";
	private static final String BRC_OPEN = "('";
	private static final String BRC_CLOSE = "')";
	String tipocombustible;
	RingtonePreference mTono;
	ListPreference lpref;
	ListPreference lpref2;
	ListPreference lpref3;
	private SharedPreferences prefs;
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getListView().setBackgroundColor(0x6a3ab2);
		addPreferencesFromResource(R.xml.prefs);
		
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	this.prefs.registerOnSharedPreferenceChangeListener(this);
		
		 PreferenceScreen prefs = getPreferenceScreen();
		 	lpref = (ListPreference) prefs.findPreference("condicion"); 
		 	lpref2 = (ListPreference) prefs.findPreference("combustible");
		 	lpref3 = (ListPreference) prefs.findPreference("vista");
		 	mTono = (RingtonePreference) prefs.findPreference("tono"); 
        	mTono.setOnPreferenceChangeListener(this);   
        	lpref.setOnPreferenceChangeListener(this);
        	lpref2.setOnPreferenceChangeListener(this);
        	lpref3.setOnPreferenceChangeListener(this);
        	try{
            String combustible = lpref2.getSharedPreferences().getString("combustible","gasoil");
            String condicion =	 lpref.getSharedPreferences().getString("condicion","cambia de precio");	
            String vista =	 lpref.getSharedPreferences().getString("vista","3D");	
        	String tono= mTono.getSharedPreferences().getString("tono","content://settings/system/notification_sound");
        	Ringtone ringtone = RingtoneManager.getRingtone(
					this, Uri.parse(tono));
        	String name = ringtone
					.getTitle(this);
        	mTono.setSummary(name);
        	lpref.setSummary(condicion);
        	lpref2.setSummary(combustible);
        	lpref3.setSummary(vista);
        	}catch(Exception e){
        		
        	
        	}
        	
	}
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
		
		
		final String stringValue = value.toString();

		if (preference instanceof ListPreference) {
			// For list preferences, look up the correct display value in
			// the preference's 'entries' list.
			ListPreference listPreference = (ListPreference) preference;
			int index = listPreference.findIndexOfValue(stringValue);

			// Set the summary to reflect the new value.
			preference
					.setSummary(index >= 0 ? listPreference.getEntries()[index]
							: null);

		} else if (preference instanceof RingtonePreference) {
			// For ringtone preferences, look up the correct display value
			// using RingtoneManager.
			if (TextUtils.isEmpty(stringValue)) {
				// Empty values correspond to 'silent' (no ringtone).
				preference.setSummary(R.string.pref_ringtone_silent);

			} else {
				Ringtone ringtone = RingtoneManager.getRingtone(
						preference.getContext(), Uri.parse(stringValue));

				if (ringtone == null) {
					// Clear the summary if there was a lookup error.
					preference.setSummary(null);
				} else {
					// Set the summary to reflect the new ringtone display
					// name.
					String name = ringtone
							.getTitle(preference.getContext());
					//preference.setSummary(name);
					mTono.setSummary(name);
					
				}
			}

		} else if (preference instanceof ListPreference) {
			// tenemos que actualizar al padre
			//SettingsCheckedChangeListener.setChecked(preference.getKey(),(Boolean)value);

			preference.setSummary(stringValue);
			
		} else {
			// For all other preferences, set the summary to the value's
			// simple string representation.
			preference.setSummary(stringValue);
			
		}
		if (stringValue.equals("gasoleo")||stringValue.equals("gasolina95")||stringValue.equals("gasolina98"))
		{
		final MainActivity ma=new MainActivity();
		final WebView webview = (WebView) MainActivity.webview;
	
		ma.runOnUiThread(new Runnable() {
	  	        @Override
	  	        public void run() {
	  	        	webview.loadUrl(JAVASCRIPT + "loadCombustible" + BRC_OPEN +stringValue+ BRC_CLOSE); 
	  	        }
	  	    });
		 ma.runOnUiThread(new Runnable() {
	  	        @Override
	  	        public void run() {
	  	        	webview.loadUrl(JAVASCRIPT + "loadCombustible" + BRC_OPEN +stringValue+ BRC_CLOSE); 
	  	        }
	  	    });
		}else if ((stringValue.equals("2D")||stringValue.equals("3D"))){
			if (stringValue.equals("2D")){
				MapActivity.vtilt=0;
			}else{
				MapActivity.vtilt=45;
			}
		}
			
		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
boolean alerta = arg0.getBoolean("alerta", false);
		if(alerta && arg1.equals("alerta") ){
		
			startService(new Intent(this, UpdaterService.class));
		       
		  		
		
		}
	}
	 @Override

	 public void onConfigurationChanged(Configuration newConfig) {

	 super.onConfigurationChanged(newConfig);

	 }
}
