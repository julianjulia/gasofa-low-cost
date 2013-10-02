package com.androidmobile.JR;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import util.utilMun;
import util.utility;
import com.androidmobile.bd.BdGas;
import com.androidmobile.model.DatosIni;
import com.androidmobile.model.Favoritos;
import com.androidmobile.model.Municipio;
import com.androidmobile.model.Provincia;
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
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

//19/08/2013
@SuppressLint("JavascriptInterface")
public class MainActivity extends Activity {
	private AdView adView;
	private LinearLayout lytMain;
	WebView webview;
	boolean salir = true;
	private static final String HTML_ROOT = "file:///android_asset/www/";
	private static final String JAVASCRIPT = "javascript:";
	private static final String BRC_OPEN = "('";
	private static final String BRC_CLOSE = "')";
	ArrayList<Provincia> alProv;
	String cod_prov = "";
	String des_prov;
	ArrayList<Provincia> alMun;
	String cod_mun = "";
	BdGas bd;
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

		try {
			alProv = Xml_BD();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 bd= new BdGas(this);
		 //DatosIni datos= bd.obtenerIni();
		 
	}

	@Override
	public void onDestroy() {
		if (adView != null)
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

			loadDialog("€ Gasofa V 2.5", "Desarrollado" + " por J.R.  "
					+ "email: jrmh@ya.com  ");
					//+ "svn: http://gasofa-low-cost.googlecode.com/svn/trunk");
			return true;
		case R.id.GasProx:
			new JavaScriptInterface(this).GasCercanas();

			// webview.loadUrl(JAVASCRIPT + "irMenOrd" + BRC_OPEN + BRC_CLOSE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void loadDialog(String titulo, String valor) {
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

			Log.i(this.getClass().toString(), "metodo readBD");
			
			Favoritos fav = new Favoritos(pos, favorito);
			bd.writerBdFavoritos(fav);

		}

		public String loadFav() {

			
			ArrayList<Favoritos> alfav = bd.readBdFav();
			String json = utility.getAllRestJSONFavoritos(alfav);
			Log.i("jsonFavoritos", json);

			return json;
		}

		public void EscribirBD(String upv) {
			salir = false;
			Intent intent = new Intent(MainActivity.this, MapActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("UPV", upv);
			bundle.putString("nombre", "");
			intent.putExtras(bundle);
			startActivity(intent);

		}

		public void loadWeb(String in) {

			Log.i(this.getClass().toString(), "metodo loadWeb");
			webview.loadUrl(in);
		}

		public void refrescar() {

		}

		public void loadPage(String in) {

			// loadshowprogres();
			Log.i(this.getClass().toString(), "metodo loadPage");
			webview.loadUrl(HTML_ROOT + in);
		}

		// TOAST POR DEFECTO
		public void loadToast(String toast) {

			Log.i(this.getClass().toString(), "metodo loadToast");
			Toast.makeText(mContext, toast + "", Toast.LENGTH_LONG).show();
		}

		public void leerGasolineras(String provincia, String municipio,
				String direccion, String num, String cp, String combustible,
				final String order) throws InterruptedException,
				ExecutionException {
				salir = false;
				DatosIni datos=new DatosIni(combustible, cod_prov, municipio, direccion, num, cp);
			if (!provincia.equals("")) {
				
				//bd.writerBdIni(datos);
				utility.tratamientoDatosGasolinera(datos, mContext);

			} else {
				if (!cp.equals("")) {
					//bd.writerBdIni(datos);
					utility.tratamientoDatosGasolinera(datos, mContext);
				} else {

					Toast.makeText(mContext, "Debe seleccionar una Provincia",
							Toast.LENGTH_LONG).show();
					webview.loadUrl(HTML_ROOT + "indexGas.html");
				}
			}

		}

		public void GasCercanas(){
			webview.loadUrl(JAVASCRIPT + "loadcomb" + BRC_OPEN
					+ BRC_CLOSE);
		
		}
		
		public void GasCercanas2(String comb) {
			
			Intent intent = new Intent(MainActivity.this, MapActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("gas", "falso");
			bundle.putString("comb", comb);
						intent.putExtras(bundle);
			startActivity(intent);
		}

		// INSERTAMOS NOTIFICACION EN BARRA DE ESTADO
		public void notBarraEstado() {

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

		public void SelectProv() {

			final CharSequence[] itemsdir = new CharSequence[alProv.size()];
			for (int i = 0; i < alProv.size(); i++) {

				itemsdir[i] = alProv.get(i).getNombre_provincia();
			}
			ContextThemeWrapper ctw = new ContextThemeWrapper(mContext,
					R.style.miestilo);
			AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
			builder.setTitle("Provincias");
			builder.setItems(itemsdir, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					des_prov = itemsdir[item].toString();
					cod_prov = alProv.get(item).getId_provincia();
					webview.loadUrl(JAVASCRIPT + "loadprov" + BRC_OPEN
							+ des_prov + BRC_CLOSE);
					dialog.cancel();
				}
			});

			AlertDialog alert = builder.create();
			alert.show();

		}

		public void SelectMun(String prov) throws ParserConfigurationException, SAXException, IOException {
			if (prov.equals("")){
				Toast.makeText(mContext, "Debe seleccionar una Provincia",
						Toast.LENGTH_LONG).show();
			}else{
				/*
			BdGas bdgas = new BdGas(mContext);
			ArrayList<Municipio> almun = bdgas.obtenerMunicipios(cod_prov);
			if (almun == null || almun.size() == 0) {
				utilMun utilmun = new utilMun();
				utilmun.loadWebMunicipios(mContext, des_prov, cod_prov);
			} else {

				dialMun(almun);
			}
			*/
				dialMun(cod_prov);
		}
		
		}
		
	}



	public void dialMun(String cod_prov) throws ParserConfigurationException, SAXException, IOException {
		ArrayList <Municipio> almun=getMunicipio(cod_prov);
		final CharSequence[] itemsdir = new CharSequence[almun.size()];
		for (int i = 0; i < almun.size(); i++) {

			itemsdir[i] = almun.get(i).getMunicipio();
		}
		ContextThemeWrapper ctw = new ContextThemeWrapper(this,
				R.style.miestilo);
		AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
		builder.setTitle("Municipios " + des_prov);
		builder.setItems(itemsdir, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				String des_mun = itemsdir[item].toString();
				webview.loadUrl(JAVASCRIPT + "loadmun" + BRC_OPEN + des_mun
						+ BRC_CLOSE);
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (salir)
				System.exit(0);

			else {
				webview.loadUrl(HTML_ROOT + "indexGas.html");
				// Toast.makeText(this, "volver a pulsar para salir" ,
				// Toast.LENGTH_LONG ).show();
				salir = true;
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public ArrayList<Provincia> ReadProvincias() {
		
		ArrayList<Provincia> alp = bd.readBdProv();
		if (alp.size() == 0)
			writeProv();
		else
			return alp;
		return null;

	}

	public void writeProv() {
		ArrayList<Provincia> alp = null;
		try {
			alp = Xml_BD();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bd.writerBdProvincias(alp);
	}

	public ArrayList<Provincia> Xml_BD() throws SAXException, IOException,
			ParserConfigurationException {
		Document doc;

		InputStream is = getResources().openRawResource(R.raw.provincias);

		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		doc = builder.parse(is);

		NodeList nl1, nl3;
		ArrayList<Provincia> listProv = new ArrayList<Provincia>();
		nl1 = doc.getElementsByTagName("provincia");
		if (nl1.getLength() > 0) {
			for (int i = 0; i < nl1.getLength(); i++) {
				Node node1 = nl1.item(i);

				nl3 = node1.getChildNodes();
				Node idNode = nl3.item(getNodeIndex(nl3, "id_provincia"));
				String id_prov = idNode.getTextContent();
				Node nomNode = nl3.item(getNodeIndex(nl3, "nombre_provincia"));
				String nombre_provincia = nomNode.getTextContent();
				listProv.add(new Provincia(id_prov, nombre_provincia));
			}

		}
		return listProv;

	}

	private static int getNodeIndex(NodeList nl, String nodename) {
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeName().equals(nodename))
				return i;
		}
		return -1;
	}
	 public ArrayList<Municipio> getMunicipio (String cod) throws ParserConfigurationException, SAXException, IOException {
		 		 
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			 utilMun um= new utilMun();
			InputStream is= um.loadWebMunicipios(this, "", cod);
			Document doc = builder.parse(is);
	       NodeList nl1;
	        ArrayList<Municipio> listMun = new ArrayList<Municipio>();
	         nl1 = doc.getElementsByTagName("m");
	         if (nl1.getLength() > 0) {
	             for (int i = 0; i < nl1.getLength(); i++) {
	                 Node node1 = nl1.item(i);
	                String nombre_municipio = node1.getTextContent();
	                nombre_municipio = nombre_municipio.replaceAll("\"", "").replaceAll("\'", "");
	                 listMun.add(new Municipio(cod , nombre_municipio));
	             }
	          
		}
			return listMun;
	   }
}
