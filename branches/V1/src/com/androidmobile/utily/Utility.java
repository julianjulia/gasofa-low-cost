package com.androidmobile.utily;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;


public class Utility {
	private static final String HTML_ROOT = "file:///android_asset/www/";
	private static final String JAVASCRIPT = "javascript:";
	@SuppressWarnings("unused")
	private static final String BRC = "()";
	private static final String BRC_OPEN = "('";
	private static final String BRC_CLOSE = "')";
	private static final String C="','";
	public static final String EMPTY_WIFI_LIST = "{\"listawifi\":[]}";
	
	static WifiManager wifi;
	static WebView webview;
	static Context mContext;
	static String essid;
	public Utility(){};


	public Utility( WifiManager wifi ,WebView webview,Context mContext,String essid) {
		this.mContext=mContext;
		this.wifi=wifi;
		this.webview=webview;
		this.essid=essid;
	}
	

	public void retardo( final int retardo) throws InterruptedException,
	ExecutionException {

class taskretardo extends AsyncTask<Void, Void, Void> {

	
	protected void onPreExecute() {
	
	}

	// Este método se ejecutará después y será el que ejecute el código
	// en segundo plano
	@Override
	protected Void doInBackground(Void... params) {
									// segundos.

			try {
				Thread.sleep(retardo);
			} catch (InterruptedException e) {
			

		return null;// Al llegar aquí, no devolvemos nada y acaba la
					// tarea en segundo plano y se llama al método
					// onPostExecute().
	}
			return null;
	}


	protected void onPostExecute(Void result) {
			WifiInfo wi=wifi.getConnectionInfo();
		    SupplicantState ss=wi.getSupplicantState();
	   	    final String estado=ss.toString();
		((Activity) mContext).runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	 webview.loadUrl(JAVASCRIPT + "ActualizarDatos" + BRC_OPEN +essid+ C + estado+ BRC_CLOSE);
	        }
	    });
	}
}
new taskretardo().execute();

}
	
	
	
	
}
