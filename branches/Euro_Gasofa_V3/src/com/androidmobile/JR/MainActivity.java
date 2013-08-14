package com.androidmobile.JR;

import java.util.ArrayList;

import java.util.concurrent.ExecutionException;

import com.adroidmobile.map.utility;
import com.androidmobile.bd.BdGas;
import com.androidmobile.model.Favoritos;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("JavascriptInterface")
public class MainActivity extends Activity {
	private AdView adView;
	private LinearLayout lytMain;
	WebView webview;
	boolean salir = true;
	private static final String HTML_ROOT = "file:///android_asset/www/";

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		// Creamos la variable webview
		webview = (WebView) findViewById(R.id.mainWebView);
		lytMain = (LinearLayout) findViewById(R.id.lytMain);
	      adView = new AdView(this, AdSize.BANNER, "a151b059515123b");
	      lytMain.addView(adView);
	      adView.bringToFront();
	      adView.loadAd(new AdRequest());
		// Activamos JavaScript en nuestro WebView
	 
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webview.addJavascriptInterface(new JavaScriptInterface(this),
				"androidSupport");

		// Cargamos la Url en nuestro WebView
		webview.loadUrl(HTML_ROOT + "indexGas.html");
		/*
		ProgressBar barraProgreso = (ProgressBar) findViewById(R.id.progressbar);

		try {
			utility.retardo(webview, 80, barraProgreso);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
	}
	@Override
	public void onDestroy(){
	      if(adView != null)
	         adView.destroy();
	      super.onDestroy();
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
			
			loadDialog("€ Gasofa V 1.0","Desarrollado"
					+ " por J.R.  " 
					+ "email: jrmh@ya.com  ")	;
			return true;
		case R.id.GasProx:
			
			Intent intent = new Intent(MainActivity.this, MapActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("gas","falso");
			intent.putExtras(bundle);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void loadDialog(String titulo,String valor) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(titulo);
		dialog.setMessage(valor);
		dialog.setPositiveButton("OK", new OnClickListener() {
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

		public void writeBDFavoritos(String favorito, int pos) {
			salir=false;
			Log.i(this.getClass().toString(), "metodo readBD");
			BdGas bdgas = new BdGas(mContext);
			Favoritos fav = new Favoritos(pos, favorito);
			bdgas.writerBdFavoritos(fav);

		}

		public String loadFav() {
			salir=false;
			BdGas bdgas = new BdGas(mContext);
			ArrayList<Favoritos> alfav = bdgas.readBdFav();
			String json = utility.getAllRestJSONFavoritos(alfav);
			Log.i("jsonFavoritos", json);

			return json;
		}

		public void EscribirBD(String n,String t) {
			salir=false;
			Intent intent = new Intent(MainActivity.this, MapActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("direccion",t);
			bundle.putString("nombre",n);
			intent.putExtras(bundle);
			startActivity(intent);
			
		}

		public void loadWeb(String in) {
			salir=false;
			Log.i(this.getClass().toString(), "metodo loadWeb");
			webview.loadUrl(in);
		}

		public  void refrescar(){
			salir=false;
					}
		
		public void loadPage(String in) {
			salir=false;
			// loadshowprogres();
			Log.i(this.getClass().toString(), "metodo loadPage");
			webview.loadUrl(HTML_ROOT + in);
		}

		// TOAST POR DEFECTO
		public void loadToast(String toast) {
			salir=false;
			Log.i(this.getClass().toString(), "metodo loadToast");
			Toast.makeText(mContext, toast + "", Toast.LENGTH_LONG ).show();
		}

		public String leerGasolineras(String gas, final String order)
				throws InterruptedException, ExecutionException {
			
			salir=false;
			//webview.loadUrl(HTML_ROOT + "indexRes.html");
			if (order == null) {		
				
				utility.tratamientoDatosGasolinera(gas, mContext, webview);

			} else {

				utility.ordenadoDatosGasolinera(order, mContext, webview);

			}

			return null;

		}
		
		public void GasCercanas(){
			salir=false;
			Intent intent = new Intent(MainActivity.this, MapActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("gas","falso");
			intent.putExtras(bundle);
			startActivity(intent);
		}

		// INSERTAMOS NOTIFICACION EN BARRA DE ESTADO
		public void notBarraEstado() {
			salir=false;
			Log.i(this.getClass().getName(), "notBarraEstado");
			// Obtenemos una referencia al servicio de notificaciones
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager notManager = (NotificationManager) getSystemService(ns);

			// Configuramos la notificación
			int icono = android.R.drawable.stat_sys_warning;
			CharSequence textoEstado = "Alerta!";
			long hora = System.currentTimeMillis();
			Notification notif = new Notification(icono, textoEstado, hora);
			// Configuramos el Intent
			Context contexto = getApplicationContext();
			CharSequence titulo = "Mensaje de Alerta";
			CharSequence descripcion = "Contacte con el Administrador";
			Intent notIntent = new Intent(contexto, MainActivity.class);
			PendingIntent contIntent = PendingIntent.getActivity(contexto, 0,
					notIntent, 0);
			notif.setLatestEventInfo(contexto, titulo, descripcion, contIntent);

			// AutoCancel: cuando se pulsa la notificaión ésta desaparece
			notif.flags |= Notification.FLAG_AUTO_CANCEL;
			notif.defaults |= Notification.DEFAULT_VIBRATE;
			// Añadir sonido, vibración y luces
			notif.defaults |= Notification.DEFAULT_SOUND;
			// notif.defaults |= Notification.DEFAULT_LIGHTS;

			// Enviar notificación
			notManager.notify(1234, notif);

		}

	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	if ((keyCode == KeyEvent.KEYCODE_BACK))
	{
		if (salir)
			System.exit(0);	
		
		else{
			webview.loadUrl(HTML_ROOT + "indexGas.html");
			//Toast.makeText(this, "volver a pulsar para salir" , Toast.LENGTH_LONG ).show();
			salir=true;
		}
			return true;
	}



	return super.onKeyDown(keyCode, event);
	}

}
