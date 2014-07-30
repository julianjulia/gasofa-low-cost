package com.androidmobile.map;

import java.io.IOException;


import java.io.InputStream;
import java.util.ArrayList;
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
import com.androidmobile.JR.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;



public class GMapGeocoderInverter {
	
	Context mContext;
	private double lat;
	private double lon;
	LatLng  UPV ;
	String url;  	
	GoogleMap mapa;
	public ArrayList<String> aldir;
	int i;
	
	// i=0 main  i=1 map 
	
	   public GMapGeocoderInverter(Context mContext, LatLng UPV,int i) {
		   this.mContext=mContext;		  
		   this.UPV=UPV;
		    lat=UPV.latitude;
			lon=UPV.longitude;
			this.i=i;
		  url="http://maps.googleapis.com/maps/api/geocode/"+"xml?latlng="+lat+","+lon+"&sensor=false";
		if(i==1){
		  SupportMapFragment fm = (SupportMapFragment) ((FragmentActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.map);
			 
          // Getting GoogleMap object from the fragment
          mapa = fm.getMap();
		}
		  
		  new taskgasDirecc().execute();
		
	}
	
	   
	
	class taskgasDirecc extends AsyncTask<Void, Void, Void> {
			//private ProgressDialog pd;
			Document doc;
					
				 
			 @Override
			protected void onPreExecute() {
				/*
				pd = new ProgressDialog(mContext);
				//pd.setTitle("Procesando Datos...");
				pd.setMessage("Espere...");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
				*/
			}

			@Override
			protected Void doInBackground(Void... args) {
				try {
					
					 HttpClient httpClient = new DefaultHttpClient();
		             HttpContext localContext = new BasicHttpContext();
		             HttpPost httpPost = new HttpPost(url);
		             httpPost.addHeader("Accept-Language", "es");
		             HttpResponse  response = httpClient.execute(httpPost, localContext);
					  InputStream in = response.getEntity().getContent();
					  DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			          doc = builder.parse(in);
			         
	    				
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
					return null;
			}

			@Override
			protected void onPostExecute(Void json) {
				//pd.dismiss();
				// error play store 01/03/2014
				//java.lang.NullPointerException	at com.androidmobile.map.GMapGeocoderInverter.getDirection(GMapGeocoderInverter.java:138)
				if(i==1){
				try{
				getDirection(doc);
				if (aldir.get(0)!=null && aldir.get(0)!="")
					 mapa.addMarker(new MarkerOptions().position(UPV).title("Ubicacion").snippet(aldir.get(0))
			      			  .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
				}catch(Exception e){
					Toast.makeText(mContext, "Error inesperado //No se puede recuperar ubicacion//",
								Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				}else{
					try{
					ArrayList<String> ald=getDirection(doc);
					
					MainActivity.ubicacion.setText(ald.get(0));
					}catch(Exception e){
						MainActivity.ubicacion.setText("sin ubicacion definida");	
					}
				}
			}
	   }

	public ArrayList<String> getDirection (Document doc) {
		   
		    aldir = new ArrayList<String>();
	        NodeList nl1;
	      
	        nl1 = doc.getElementsByTagName("result");
	        if (nl1.getLength() > 0) {
	            for (int i = 0; i < nl1.getLength(); i++) {
	            	// obtener la direccion
	            	NodeList nlname = doc.getElementsByTagName("formatted_address");
	            	Node  nodename = nlname.item(i);
	            	aldir.add(nodename.getTextContent());
	            	               
	        }
	        }
	        return aldir;
			
	   }
	        
}
