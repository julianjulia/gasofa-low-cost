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
	WifiManager wifiManager;
	static WifiInfo wi;
	static WebView webview;
	static Context mContext;
	static String essid;
	public Utility(){};
	public Utility(WifiManager wifiManager,WebView webview,Context mContext) {
		this.mContext=mContext;
		this.wifiManager = wifiManager;
		this.webview=webview;
	}

	public Utility( WifiInfo wi ,WebView webview,Context mContext,String essid) {
		this.mContext=mContext;
		this.wi=wi;
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
	
	
	
	public void resultadosWifi() {
		String etWifiList = null;
		List<ScanResult> results = wifiManager.getScanResults();
		String message = "No results. Check wireless is on";
		if (results != null) {
			final int size = results.size();
			if (size == 0)
				message = "No access points in range";
			else {
				ScanResult bestSignal = results.get(0);
				etWifiList = (""); // etWifiList is EditText
				int count = 1;
				for (ScanResult result : results) {
					//if((result.SSID.indexOf("WLAN_")!=-1) || (result.SSID.indexOf("JAZZTEL_")!=-1)  )
					webview.loadUrl(JAVASCRIPT + "GenerarKey" + BRC_OPEN
							+ result.SSID+"','"+result.BSSID + BRC_CLOSE);
					
					etWifiList = count++ + ". " + result.SSID + " : "
							+ result.level + "\n" + result.BSSID + "\n"
							+ result.capabilities + "\n"
							+ "\n=======================\n";
					Log.i("wifi", etWifiList);
					// Toast.makeText(mContext,etWifiList, Toast.LENGTH_LONG).show();
				}
			}
		}
		// Toast.makeText(this, message, Toast.LENGTH_LONG).show();

	}
	
}
