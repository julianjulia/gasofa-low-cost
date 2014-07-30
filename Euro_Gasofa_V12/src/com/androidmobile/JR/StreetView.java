package com.androidmobile.JR;


import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;




public class StreetView extends Activity {
	WebView webview;
	

	//private static final String JAVASCRIPT = "javascript:";
	//private static final String BRC_OPEN = "('";
	//private static final String Q = "','";
	//private static final String BRC_CLOSE = "')";
	private static final String HTML_ROOT = "file:///android_asset/www/";
	

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_street);
		webview = (WebView) findViewById(R.id.StreetWebView);
		
		
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		
		
				
		
	
		webview.addJavascriptInterface(new JavaScriptInterface (this),"androidSupportSV");
		Bundle bundle = getIntent().getExtras();
		final double lat= bundle.getDouble("lat");
		final double lon= bundle.getDouble("lon");
		//final double lon=37.869260;
		//final double lat=-122.254811;
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	webview.loadUrl(HTML_ROOT + "indexStreetView.html?lon="+lon+"&lat="+lat);
	        }
	    });
		
		/*
		 runOnUiThread(new Runnable() {
	  	        @Override
	  	        public void run() {
	  	        	webview.loadUrl(JAVASCRIPT + "cargaUPV" + BRC_OPEN +lon+Q+lat+ BRC_CLOSE); 
	  	        }
	  	    });
		
		 runOnUiThread(new Runnable() {
	  	        @Override
	  	        public void run() {
	  	        	webview.loadUrl(JAVASCRIPT + "cargaUPV" + BRC_OPEN +lon+Q+lat+ BRC_CLOSE); 
	  	        }
	  	    });
*/
	}
	public class JavaScriptInterface {
		Context mContext;

		JavaScriptInterface(Context c) {
			mContext = c;
		}

		public void EscribirBD(String n,String t) {
			
			
		}
		
		public void loadToast(String toast) {
			Log.i(this.getClass().toString(), "metodo loadToast");
			Toast.makeText(mContext, toast + "", Toast.LENGTH_LONG ).show();
		}
		
		
		
		
	}
	
	/*
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		   if ( keyCode == KeyEvent.KEYCODE_BACK ) {
	            
	            return true;
	        }
		return super.onKeyDown(keyCode, event);
	}
	*/
	
	
	 @Override

	 public void onConfigurationChanged(Configuration newConfig) {

	 super.onConfigurationChanged(newConfig);

	 }
}
