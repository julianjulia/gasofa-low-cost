package com.androidmobile.JR;


import java.io.StringWriter;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

import com.androidmobile.JR.MainActivity.JavaScriptInterface;
import com.androidmobile.bd.BdGas;
import com.androidmobile.model.Alerta;
import com.androidmobile.model.ListaAlertas;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;


public class MyAlarmasActivity extends FragmentActivity {
	
	static WebView webview;
	public static boolean salir = true;
	private static final String HTML_ROOT = "file:///android_asset/www/";
	private static final String JAVASCRIPT = "javascript:";
	private static final String BRC_OPEN = "('";
	private static final String BRC_CLOSE = "')";
	private static final String Q = "','";
	public static final String EMPTY_ALERT_LIST = "{\"listaalertas\":[]}";
	public String json;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_messages);
		webview = (WebView) findViewById(R.id.AlertWebView);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webview.addJavascriptInterface(new JavaScriptInterface(this),
				"androidSupportAlerta");
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	webview.loadUrl(HTML_ROOT + "indexAlerta.html");
	        }
	    });
		
	}
	
	public class JavaScriptInterface {

		Context mContext;

		JavaScriptInterface(Context c) {
			mContext = c;
		}
		public void ReadAlert() {
			inicio();
			Log.i(this.getClass().toString(), "listar alertas " );
			
		}
		public void inicioMain() {
			
			Log.i(this.getClass().toString(), "inicio Main" );
			Intent intent = new Intent(mContext, MainActivity.class);
			startActivity(intent);
		}
	}
	
	public void inicio() {
		BdGas bd = new BdGas(this);
		ArrayList<Alerta> alalert=bd._getStatusAlertas();
	 	json= getAllRestJSONAlertas(alalert);
		
		if (json.equals(EMPTY_ALERT_LIST)){ 	
			Toast.makeText(this, "No hay alertas Activas",
			Toast.LENGTH_LONG).show();
		}else{
			Log.i("Alertas activas", json);
			Log.i(this.getClass().toString(), "metodo loadPage");
			runOnUiThread(new Runnable() {
		        @Override
		        public void run() {
		        	webview.loadUrl(JAVASCRIPT + "setRestList" + BRC_OPEN + json 
							+ BRC_CLOSE);
		        }
		    });
			
	}
		if (alalert!=null){
		for (Alerta al: alalert){
			bd.updateAlerta(al.getUPV(), al.getId_comb(), al.getCondicion(), "false");
		}
		}
	}
	public static String getAllRestJSONAlertas(
			ArrayList<Alerta> alalertas) {
		StringWriter writer = new StringWriter();
		ListaAlertas listaalertas = new ListaAlertas();
		listaalertas.setListaalertas(alalertas)  ;
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(writer, listaalertas);
		} catch (Exception e) {

			return EMPTY_ALERT_LIST;
		}

		return writer.toString();
	}
}
