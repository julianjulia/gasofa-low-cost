package com.androidmobile.JR;

import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.androidmobile.bd.BdGas;
import com.androidmobile.model.DatosIni;
import com.androidmobile.model.Municipio;
import com.androidmobile.model.Provincia;
import com.androidmobile.util.utilMun;
import com.androidmobile.util.utility;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

//19/08/2013
@SuppressLint("JavascriptInterface")
public class MainActivity extends FragmentActivity {
	private AdView adView;
	private LinearLayout lytMain;
	WebView webview;
	boolean salir = true;
	private static final String HTML_ROOT = "file:///android_asset/www/";
	private static final String JAVASCRIPT = "javascript:";
	private static final String BRC_OPEN = "('";
	private static final String BRC_CLOSE = "')";
	ArrayList<Provincia> alProv;
	public static String cod_prov = "";
	public static String des_prov;
	public static String des_mun = "";
	ArrayList<Provincia> alMun;
	public static String direccion = "";
	public static String num="";
	public static String cp="";
	public static String comb="";
	BdGas bd;
	
	public static SlidingMenu slidingMenu ;
	public static SlidingMenu slidingFavoritos ;
	public static FragmentActivity actividad;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		actividad =this;
		slidingMenu = new SlidingMenu(this);
	    slidingMenu.setMode(SlidingMenu.LEFT);
	    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	    slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
	    slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
	    slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	    slidingMenu.setFadeDegree(0.35f);
	    slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	    slidingMenu.setMenu(R.layout.slidingmenu);

	    // FAVORITOS
	    slidingFavoritos = new SlidingMenu(this);
	    slidingFavoritos.setMode(SlidingMenu.RIGHT);
	    slidingFavoritos.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	    slidingFavoritos.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
	    slidingFavoritos.setShadowDrawable(R.drawable.slidingmenu_shadow);
	    slidingFavoritos.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	    slidingFavoritos.setFadeDegree(0.35f);
	    slidingFavoritos.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	    slidingFavoritos.setMenu(R.layout.slidingfavoritos);
        
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
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	webview.loadUrl(HTML_ROOT + "indexGas.html");
	        }
	    });
	

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
	public void onBackPressed() {
		 if ( slidingMenu.isMenuShowing()) {
	            slidingMenu.toggle();
	        }
	        else {
	            super.onBackPressed();
	        }
	}
	
	@Override
	public void onDestroy() {
		if (adView != null)
			adView.destroy();
		super.onDestroy();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	if ( slidingMenu.isMenuShowing()) {
	            slidingMenu.toggle();
	        }else if(slidingFavoritos.isMenuShowing()){
	        	slidingFavoritos.toggle();
	        }else{
	        	slidingMenu.toggle();
	        }
           
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

		public void fav() {

			Log.i(this.getClass().toString(), "metodo fav");
			runOnUiThread(new Runnable() {
		        @Override
		        public void run() {
		        	slidingFavoritos.toggle();
		        }
		    });

		}

		public void LoadDatos(String Direccion,String Num,String Cp,String Comb){
			direccion=Direccion;
			num=Num;
			cp=Cp;
			comb=Comb;
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

		public void loadWeb(final String in) {

			Log.i(this.getClass().toString(), "metodo loadWeb");
			runOnUiThread(new Runnable() {
		        @Override
		        public void run() {
		        	webview.loadUrl(in);
		        }
		    });
			
		}

		public void refrescar() {

		}

		public void loadPage(final String in) {

			// loadshowprogres();
			Log.i(this.getClass().toString(), "metodo loadPage");
			runOnUiThread(new Runnable() {
		        @Override
		        public void run() {
		        	webview.loadUrl(HTML_ROOT +in);
		        }
		    });
			
		}

		// TOAST POR DEFECTO
		public void loadToast(String toast) {

			Log.i(this.getClass().toString(), "metodo loadToast");
			Toast.makeText(mContext, toast + "", Toast.LENGTH_LONG).show();
		}

		public void menu() {
			runOnUiThread(new Runnable() {
		        @Override
		        public void run() {
		        
		        	slidingMenu.toggle();
		        }
		    });
		
		        
		}
		
		public void leerGasolineras(String provincia, String municipio,
				String direccion, String num, String cp, String combustible,
				final String order) throws InterruptedException,
				ExecutionException {
				salir = false;
				long fecha=new Date().getTime();
				DatosIni datos=new DatosIni(combustible, cod_prov,des_prov, municipio, direccion, num, cp,fecha);
			if (!provincia.equals("")) {
				bd.updateBdFavoritos(datos);
				//bd.writerBdIni(datos);
				utility.tratamientoDatosGasolinera(datos, mContext);

			} else {
				if (!cp.equals("")) {
					bd.updateBdFavoritos(datos);
					utility.tratamientoDatosGasolinera(datos, mContext);
				} else {

					Toast.makeText(mContext, "Debe seleccionar una Provincia",
							Toast.LENGTH_LONG).show();
					runOnUiThread(new Runnable() {
				        @Override
				        public void run() {
				        	webview.loadUrl(HTML_ROOT + "indexGas.html");
				        }
				    });
					
				}
			}
			runOnUiThread(new Runnable() {
		        @Override
		        public void run() {
		        	slidingFavoritos.setMenu(R.layout.slidingfavoritos);
		        }
		    });
			
		}

	
		
		public void GasCercanas(){
			runOnUiThread(new Runnable() {
		        @Override
		        public void run() {
		        	webview.loadUrl(JAVASCRIPT + "loadcomb" + BRC_OPEN
							+ BRC_CLOSE); 
		        }
		    });
		
		
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

			// Configuramos la notificaci�n
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

			// AutoCancel: cuando se pulsa la notificai�n �sta desaparece
			notif.flags |= Notification.FLAG_AUTO_CANCEL;
			notif.defaults |= Notification.DEFAULT_VIBRATE;
			// A�adir sonido, vibraci�n y luces
			notif.defaults |= Notification.DEFAULT_SOUND;
			// notif.defaults |= Notification.DEFAULT_LIGHTS;

			// Enviar notificaci�n
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
					dialog.cancel();
					EnvProv();
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
				dialog.cancel();
				EnvMun(des_mun);
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}
	public void EnvProv() {
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	  webview.loadUrl(JAVASCRIPT + "loadprov" + BRC_OPEN
		    				+ des_prov + BRC_CLOSE);
	        }
	    });
	        
	  
		
	}
	public void EnvMun(final String Des_mun){
		des_mun=Des_mun;
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	webview.loadUrl(JAVASCRIPT + "loadmun" + BRC_OPEN + Des_mun
	    				+ BRC_CLOSE); 
	        }
	    });
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (salir){
				ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.style.miestilo);
				AlertDialog.Builder alert = new AlertDialog.Builder(ctw);
				alert.setMessage("Salir de Gasofa");
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						System.exit(0);
					}
					});
				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int whichButton) {
					    // Canceled.
					  }
					});

					alert.show();	
						 
				}else {
				runOnUiThread(new Runnable() {
			        @Override
			        public void run() {
			        	webview.loadUrl(HTML_ROOT + "indexGas.html");
			        }
			    });
				// Toast.makeText(this, "volver a pulsar para salir" ,
				// Toast.LENGTH_LONG ).show();
				salir = true;
			}
			return true;
		}
		   if ( keyCode == KeyEvent.KEYCODE_MENU ) {
				if ( slidingMenu.isMenuShowing()) {
		            slidingMenu.toggle();
		        }else if(slidingFavoritos.isMenuShowing()){
		        	slidingFavoritos.toggle();
		        }else{
		        	slidingMenu.toggle();
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
