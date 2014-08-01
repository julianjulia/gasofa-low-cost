package com.androidmobile.service;

import java.io.IOException;



import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.androidmobile.JR.MainActivity;
import com.androidmobile.JR.MyAlarmasActivity;
import com.androidmobile.bd.BdGas;
import com.androidmobile.model.Alerta;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;

public class UpdaterService extends Service {

	private final int PERIODIC_EVENT_MIN = 60 * 4 ; // 1/2 hora
	//private final int PERIODIC_EVENT_MIN = 60 ; 
	private final int HORA_INICIO = 9;
	private final int HORA_FINAL = 23;
	final String TAG = "UpdaterService";
	private SharedPreferences prefs;
	public NotificationManager nm;
	private static final int ID_NOTIFICACION_CREAR = 1;
	AlarmManager alarmManager;
	PendingIntent pendingIntent;
	BdGas bdgas;
	
	public static boolean runflag = false;

	public static boolean vibrate = true;
	public static boolean sound = true;
	public static boolean alerta = true;
	public static String condicion = "distinto";
	public static String tono ="";
	private static String id_comb = "3";
	
	String datos;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("static-access")
	@Override
	public void onCreate() {
		
		this.runflag = true;
		Log.d(TAG, "onCreated");
		//nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		bdgas = new BdGas(getApplicationContext());

		super.onCreate();
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		alerta = prefs.getBoolean("alerta", false);
		vibrate = prefs.getBoolean("vibrate", false);
		//sound = prefs.getBoolean("sound", false);
		sound = true;
		condicion = prefs.getString("condicion", "cambia de precio");
		tono = prefs.getString("tono", "content://settings/system/notification_sound");
		
		if (alerta){
			iniciarServicio();
		}
		stopSelf();
		return START_NOT_STICKY;
	}

	public void iniciarServicio() {

		Intent intent = new Intent(this, this.getClass());
		pendingIntent = PendingIntent.getService(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		long currentTimeMillis = System.currentTimeMillis();
		long nextUpdateTimeMillis = currentTimeMillis + PERIODIC_EVENT_MIN
				* DateUtils.MINUTE_IN_MILLIS;
		Time nextUpdateTime = new Time();
		nextUpdateTime.set(nextUpdateTimeMillis);
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		if ((nextUpdateTime.hour - (PERIODIC_EVENT_MIN/60) > 0) &&(nextUpdateTime.hour < HORA_INICIO
				|| nextUpdateTime.hour - (PERIODIC_EVENT_MIN/60) > HORA_FINAL)  ) {
			//alarmManager.cancel(pendingIntent);
			nextUpdateTime.hour = HORA_INICIO;
			//nextUpdateTime.minute = 0;
			//nextUpdateTime.second = 0;
			nextUpdateTimeMillis = nextUpdateTime.toMillis(false);
			
			if (nextUpdateTime.hour - (PERIODIC_EVENT_MIN/60) > HORA_FINAL )
				nextUpdateTimeMillis = nextUpdateTimeMillis + DateUtils.DAY_IN_MILLIS;
						
			alarmManager.set(AlarmManager.RTC, nextUpdateTimeMillis, pendingIntent);
		}else{
			alarmManager.set(AlarmManager.RTC, nextUpdateTimeMillis, pendingIntent);
			try{
			ArrayList<Alerta> alert = bdgas.readBdAlert(null);
			String[] upv= new String[alert.size()];
			for (int i=0; i<alert.size();i++){
			Alerta alerta = alert.get(i);
			id_comb = alerta.getId_comb();
			String UPV = alerta.getUPV();
			UPV=UPV+";"+id_comb+":"+alerta.getPvp();
			upv[i]=UPV;
		   	datos = "";
			datos = alerta.getNombre() + " " + "A:" + alerta.getPvp();
			Log.d(TAG, "notibarra");
			}
			new taskgasP().execute(upv);
			}catch(Exception e){
			  Log.d(TAG,"Exception bbdd");
			}
			
		}		
		
	}

	class taskgasP extends AsyncTask<String, Void, Long> implements
			OnCancelListener {
		double lon=0.00;
		double lat=0.00;
		double variable = 0.0001;
		
		Document doc;
		
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Long doInBackground(String... args) {
		
				double pvp = 0;
		        int count = args.length;
		        long totalSize = 0;	
		        datos ="";	
		        for (int i = 0; i < count; i++) {
		        	try {		 
		        String UPV =args[i];
		        int com = UPV.indexOf(",");
		        int pcoma= UPV.indexOf(";");
		        int dpunto= UPV.indexOf(":");
		    	lon = Double.parseDouble(UPV.substring(0, com));
		    	lat = Double.parseDouble(UPV.substring(com + 1,pcoma));
		    	id_comb= UPV.substring(pcoma+1,dpunto);
		    	double p_comb=Double.parseDouble(UPV.substring(dpunto+1));
		    	String url = "http://www.geoportalgasolineras.es/queryPopUp.do?urlValor=http://geoportalgasolineras.es/cgi-bin/mapserv?SERVICE=WFS&VERSION=1.0.0&REQUEST=GetFeature&TYPENAME=estaciones_servicio_brief&BBOX="
						+ (lon - variable)
						+ ","
						+ (lat - variable)
						+ ","
						+ (lon + variable)
						+ ","
						+ (lat + variable)
						+ "&tipoCarburante=" + id_comb + "&tipoBusqueda=0";	
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(url);
				httpPost.addHeader("Accept-Language", "es");
				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				InputStream in = response.getEntity().getContent();
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				doc = builder.parse(in);
				pvp = getDirection(doc);
				String Str_lon= corrector(lon+"",6);
				String Str_lat= corrector(lat+"",6);
				String UPV_C=Str_lon+","+Str_lat;
				if (condicion.equals("aumento de precio") && pvp > p_comb ){
					totalSize++;
					datos+=pvp+" ";
					bdgas.updateAlerta(UPV_C, id_comb, pvp+"","true");
				}else if (condicion.equals("disminuye el precio") && pvp < p_comb ){
					totalSize++;
					datos+=pvp+" ";
					bdgas.updateAlerta(UPV_C, id_comb, pvp+"","true");
				}else if(condicion.equals("cambia de precio") && pvp != p_comb ){
					totalSize++;
					datos+=pvp+" ";
					bdgas.updateAlerta(UPV_C, id_comb, pvp+"","true");
				}/*else if(pvp != p_comb){
					totalSize++;
					datos+=pvp+" ";
					bdgas.updateAlerta(lon+","+lat, id_comb, pvp+"","true");
				}*/
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		        	}
		        datos = totalSize+" "+ datos;
			return  totalSize;
		}

		private String corrector(String string, int i) {
			 int com = string.indexOf(".");
		     String entero = string.substring(0, com).trim();
		     String decimal = string.substring(com + 1).trim();
			int l = decimal.length();
			int x=i-l;
			if (i>l){
				for(int a=0;a<x;a++){
					decimal=decimal+"0";
				}
			}
			return entero+"."+decimal;
		}

		@Override
		protected void onPostExecute(Long total) {
			try {
				
				String datosfinales="Hay "+total+" Alertas";
				//datosfinales = datos + " N:" + String.valueOf(pvp);
				//datosfinales="Tienes "+ total + " Alertas";
				if(total > 0)
					notificacionBarraEstado(datosfinales);
			} catch (Exception e) {
				Log.d(TAG, "error en peticion");
			}

		}

		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub

		}

		

	}

	@SuppressWarnings("unused")
	public double getDirection(Document doc) {

		NodeList nl1, nl2;
		nl1 = doc.getElementsByTagName("elemento");
		if (nl1.getLength() > 0) {
			for (int i = 0; i < nl1.getLength(); i++) {
				// obtener el nombre

				Node node1 = nl1.item(i);
				nl2 = node1.getChildNodes();

				Node pvNode = nl2.item(getNodeIndex(nl2, "precio"));
				Double precio = Double.parseDouble(pvNode.getTextContent()
						.replace(",", "."));

				return precio;

			}
		}
		return 0.0001;

	}

	private int getNodeIndex(NodeList nl, String nodename) {
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeName().equals(nodename))
				return i;
		}
		return -1;
	}

	@SuppressLint("SimpleDateFormat")
	public void notificacionBarraEstado(String gas) {
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Date date = new Date();
		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		String fecha_hora = " " + hourdateFormat.format(date);
		int drawableResource = getApplicationContext().getResources()
				.getIdentifier("ic_launcher2", "drawable",
						getApplicationContext().getPackageName());
		Notification notification;
		
		if (Integer.valueOf(android.os.Build.VERSION.SDK) < android.os.Build.VERSION_CODES.HONEYCOMB) {
			PendingIntent intencionPendiente = PendingIntent.getActivity(
    				this, 0, new Intent(this,
    						MyAlarmasActivity.class), 0);
            notification = new Notification(drawableResource,
    				"Alerta Gasofa", System.currentTimeMillis());
            notification.setLatestEventInfo(this, fecha_hora,
    				gas, intencionPendiente);
            // notification.flags |= Notification.FLAG_AUTO_CANCEL;
           // mNM.notify(NOTIFICATION, notification);
        } else {
        	PendingIntent intencionPendiente = PendingIntent.getActivity(
    				this,new Random().nextInt(), new Intent(this,
    						MyAlarmasActivity.class), 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    this);
            notification = builder.setContentIntent(intencionPendiente)
                    .setSmallIcon(drawableResource).setTicker("Alerta Gasofa").setWhen(System.currentTimeMillis())
                    .setAutoCancel(true).setContentTitle(fecha_hora)
                    .setContentText(gas).build();

           // mNM.notify(NOTIFICATION, notification);
        }
			
		// AutoCancel: cuando se pulsa la notificaión ésta desaparece
		//notification.flags |= Notification.FLAG_AUTO_CANCEL;
		if (vibrate)
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		// Añadir sonido, vibración y luces
		if (sound)
			notification.sound = Uri.parse(tono);
			 
			//notificacion.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		//nm.notify(ID_NOTIFICACION_CREAR, notification);
		nm.notify(ID_NOTIFICACION_CREAR, notification);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onDestroy() {
		//nm.cancel(ID_NOTIFICACION_CREAR);
		this.runflag = false;
		Log.d(TAG, "onDestroyed");

		super.onDestroy();
	}

	
}
