package com.androidmobile.utily;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
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
	public static final String EMPTY_WIFI_LIST = "{\"listawifi\":[]}";
	WifiManager wifiManager;
	WebView webview;
	Context mContext;
	
	public Utility(WifiManager wifiManager,WebView webview,Context mContext) {
		this.mContext=mContext;
		this.wifiManager = wifiManager;
		this.webview=webview;
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
