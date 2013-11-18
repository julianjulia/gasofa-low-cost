package com.androidmobile.Wificon;

import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.map.ObjectMapper;

import com.androidmobile.model.ListaWifi;
import com.androidmobile.model.Wifi;
import com.androidmobile.utily.Utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	WebView webview;
	private static final String HTML_ROOT = "file:///android_asset/www/";
	private static final String JAVASCRIPT = "javascript:";
	@SuppressWarnings("unused")
	private static final String BRC = "()";
	private static final String BRC_OPEN = "('";
	private static final String C="','";
	private static final String BRC_CLOSE = "')";
	public static final String EMPTY_WIFI_LIST = "{\"listawifi\":[]}";
	Utility uti;
	WifiManager wifiManager;
	int bescanear = 0;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Creamos la variable webview
		webview = (WebView) findViewById(R.id.mainWebView);
		Button button = (Button) findViewById(R.id.buttonScaner);

		// Activamos JavaScript en nuestro WebView

		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webview.addJavascriptInterface(new JavaScriptInterface(this),
				"androidSupport");
		webview.loadUrl(HTML_ROOT + "WPAMagicKey.html");
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		uti = new Utility(wifiManager, webview, getApplicationContext());
		if (!wifiManager.isWifiEnabled()) {
			loadDialog("ACTIVAR WIFI", true);

		} else {

			resultadosWifi();

		}

	}

	public void activarWifi(Integer sw) {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		switch (sw) {
		case 0:
			try {
				wifiManager.setWifiEnabled(false);
				Toast toast = Toast.makeText(getApplicationContext(),
						"WiFi desactivado", Toast.LENGTH_LONG);
				toast.show();
			} catch (Exception e) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Error al desactivar WiFi", Toast.LENGTH_LONG);
				toast.show();
			}
		case 1:
			try {
				wifiManager.setWifiEnabled(true);
				Toast toast = Toast.makeText(getApplicationContext(),
						"WiFi activado", Toast.LENGTH_LONG);
				toast.show();
			} catch (Exception e) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Error al activar WiFi", Toast.LENGTH_LONG);
				toast.show();
			}
			break;
		default:
			break;
		}
	}

	public void loadDialog(String valor, final boolean b) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(valor);
		dialog.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (b)
					activarWifi(1);
				dialog.cancel();
			}
		});
		dialog.setNegativeButton("CANCELAR", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.cancel();
			}
		});
		dialog.show();
	}

	public class JavaScriptInterface {
		Context mContext;

		JavaScriptInterface(Context c) {
			mContext = c;
		}

		public void loadWeb(String in) {
			Log.i(this.getClass().toString(), "metodo loadWeb");
			webview.loadUrl(in);
		}

		public void refrescar() {
			try {
				new Utility().retardo(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// TOAST POR DEFECTO
		public void loadToast(String toast) {
			Log.i(this.getClass().toString(), "metodo loadToast");
			Toast.makeText(mContext, toast + "", Toast.LENGTH_SHORT).show();
		}

		public void loadKey(String e, String k) {
			Log.i(this.getClass().toString(), "metodo loadKey");
			conectarWifi(e,k);
		}

		public void loadScaner(String key) {
			Log.i(this.getClass().toString(), "metodo loadScaner");
			
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.exit:
			System.exit(0);
			return true;
		case R.id.help:

			loadDialog("Esta aplicacion es una version "
					+ "Beta creada para el desarrollo "
					+ "conocimiento de la programacion"
					+ "en Android, pudiendo tener   "
					+ "posibles bug....               "
					+ "                               "
					+ "Creado y Desarrollado by J.R.", false);

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void Escanear(View view) {
		if (!wifiManager.isWifiEnabled()) {
			loadDialog("ACTIVAR WIFI", true);
		} else {
			resultadosWifi();
		}
	}

	public void resultadosWifi() {
	
		if (bescanear > 0)
			Toast.makeText(this, " Buscando redes disponibles ",
					Toast.LENGTH_LONG).show();
		bescanear++;
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
				ArrayList<Wifi> allWifi = allWifi = new ArrayList<Wifi>();

				for (ScanResult result : results) {
					Wifi wifi = null;
					//if ((result.SSID.indexOf("WLAN_") != -1)
						//	|| (result.SSID.indexOf("JAZZTEL_") != -1)
						//	|| (result.SSID.indexOf("Vodafone") != -1)) {
						wifi = new Wifi(result.SSID, result.BSSID,
								result.capabilities,
								String.valueOf(result.level),
								String.valueOf(result.frequency), "NO");

						etWifiList = count++ + ". " + result.SSID + " : "
								+ result.level + "\n" + result.BSSID + "\n"
								+ result.capabilities + "\n"
								+ "\n=======================\n";
						Log.i("wifi", etWifiList);
						allWifi.add(wifi);
				//	}
					// Toast.makeText(mContext,etWifiList,
					// Toast.LENGTH_LONG).show();
				}
				String result = getAllRestJSONWifi(allWifi);
				Log.i("jsonResultados", result);
				webview.loadUrl(JAVASCRIPT + "EnviarDatos" + BRC_OPEN + result
						+ BRC_CLOSE);
				if (result.equals(EMPTY_WIFI_LIST) && bescanear > 1) {
					Toast.makeText(
							this,
							"No se han encontrado redes\nWLAN_XXXX\nJAZZTEL_XXXX\nVodafoneXXXX",
							Toast.LENGTH_LONG).show();

				}
			}
		}
		// Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	public static String getAllRestJSONWifi(ArrayList<Wifi> allWifi) {
		StringWriter writer = new StringWriter();
		ListaWifi listawifi = new ListaWifi();
		listawifi.setListawifi(allWifi);
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(writer, listawifi);
		} catch (Exception e) {
			return EMPTY_WIFI_LIST;
		}
		return writer.toString();
	}
	
			
	public void conectarWifi(final String essid,String key){
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiConfiguration wc = new WifiConfiguration();
	    // This is must be quoted according to the documentation 
	    // http://developer.android.com/reference/android/net/wifi/WifiConfiguration.html#SSID
		  Toast.makeText(this,essid +"\nKey: "+key ,Toast.LENGTH_LONG).show();
	    wc.SSID = "\""+essid+"\"";
	    wc.preSharedKey  = "\""+key+"\"";
	    wc.hiddenSSID = false;
	    wc.status = WifiConfiguration.Status.ENABLED;        
	    wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
	    wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
	    wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
	    wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
	    wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
	    wifi.saveConfiguration();
	    int res = wifi.addNetwork(wc);
	    Log.d("WifiPreference", "add Network returned " + res );
	    boolean b = wifi.enableNetwork(res, true);        
	    Log.d("WifiPreference", "enableNetwork returned " + b );
	    WifiInfo wi=wifi.getConnectionInfo();
	    SupplicantState ss=wi.getSupplicantState();
   	    final String estado=ss.toString();
   	    new Utility(wi, webview, this, essid);
   	    runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	 webview.loadUrl(JAVASCRIPT + "ActualizarDatos" + BRC_OPEN +essid+ C + estado+ BRC_CLOSE);
	        }
	    });
   	  
	}
	
	
}
