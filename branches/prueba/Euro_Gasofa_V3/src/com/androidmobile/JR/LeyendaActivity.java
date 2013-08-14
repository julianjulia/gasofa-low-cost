package com.androidmobile.JR;
import java.util.ArrayList;

import org.json.JSONArray;

import com.adroidmobile.map.GMapV2Direction;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;
// he modificado para probar
@SuppressLint("JavascriptInterface")
public class LeyendaActivity extends Activity {
	WebView webview;
	private AdView adView ;
	private LinearLayout lytMain;
	private static final String JAVASCRIPT = "javascript:";
	private static final String BRC_OPEN = "('";
	private static final String BRC_CLOSE = "')";
	private static final String HTML_ROOT = "file:///android_asset/www/";
	JSONArray array ;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_leyenda);
		webview = (WebView) findViewById(R.id.LeyWebView);
		lytMain = (LinearLayout) findViewById(R.id.lytMain);
	      adView = new AdView(this, AdSize.BANNER, "a151b059515123b");
	      lytMain.addView(adView);
	      adView.bringToFront();
	      adView.loadAd(new AdRequest());
		
		
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		
		
		ArrayList<String> al=new GMapV2Direction().al;
		
		if (al==null){
			al=new ArrayList<String>();
			al.add("NO HAY RUTA SELECCIONADA");
			
		}
		webview.addJavascriptInterface(new JavaScriptInterface (this),"androidSupportley");
		
		array = new JSONArray(al);
		
		webview.loadUrl(HTML_ROOT + "indexLey.html");
			

	}
	public class JavaScriptInterface {
		Context mContext;

		JavaScriptInterface(Context c) {
			mContext = c;
		}

		public void EscribirBD(String n,String t) {
			Intent intent = new Intent(LeyendaActivity.this, MapActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("direccion",t);
			bundle.putString("nombre",n);
			intent.putExtras(bundle);
			startActivity(intent);
			
		}
		
		public void loadToast(String toast) {
			Log.i(this.getClass().toString(), "metodo loadToast");
			Toast.makeText(mContext, toast + "", Toast.LENGTH_LONG ).show();
		}
		
		public void writeLeyenda() {
			
			webview.loadUrl(JAVASCRIPT + "showLey" + BRC_OPEN
					+ array + BRC_CLOSE);

		}
		
		
	}
}
