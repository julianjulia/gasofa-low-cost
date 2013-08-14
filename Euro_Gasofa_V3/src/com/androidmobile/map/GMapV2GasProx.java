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

import com.androidmobile.JR.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


public class GMapV2GasProx {
	GoogleMap mapa;
	private Context mContext;	
	private double lat;
	private double lon;
	LatLng  UPV ;
	String url;  	
	ArrayList<String> alname;
	ArrayList<String> aldir;
	   public GMapV2GasProx(Context mContext, GoogleMap mapa,LatLng UPV ){
		   this.mContext=mContext;
		   this.mapa=mapa;
		   this.UPV=UPV;
		    lat=UPV.latitude;
			lon=UPV.longitude;
			
		  url="https://maps.googleapis.com/maps/api/place/textsearch/"
					+"xml?location="+lat+","+lon+"&query=gasolineras&radius=2000&sensor=true&key=AIzaSyBbew32IyIjmhVtRqOuw1VDVeESwyJUMjk";
		  
		  new taskgasP().execute();
	   }
	
	   class taskgasP extends AsyncTask<Void, Void, Void> {
			private ProgressDialog pd;
			Document doc;
			
			
			
				 
			 @Override
			protected void onPreExecute() {
				
				pd = new ProgressDialog(mContext);
				//pd.setTitle("Procesando Datos...");
				pd.setMessage("Espere...");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
				
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
				pd.dismiss();
				
				ArrayList<LatLng> lt= getDirection(doc);
				 mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV,14));
				for (int i = 0;i<lt.size();i++){
				
	             mapa.addMarker(new MarkerOptions().position(lt.get(i)).title(alname.get(i)).snippet(aldir.get(i))
	            		 .icon(BitmapDescriptorFactory.fromResource(R.drawable.gazstation)));
				}
			}
	   }
	  
	   public ArrayList<LatLng> getDirection (Document doc) {
		    alname = new ArrayList<String>();
		    aldir = new ArrayList<String>();
	        NodeList nl1, nl2, nl3;
	        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
	        nl1 = doc.getElementsByTagName("geometry");
	        if (nl1.getLength() > 0) {
	            for (int i = 0; i < nl1.getLength(); i++) {
	            	 // obtener el nombre
	            	 NodeList nlname = doc.getElementsByTagName("name");
	                 Node nodename = nlname.item(i);
	            	alname.add(nodename.getTextContent());
	            	// obtener la direccion
	            	  nlname = doc.getElementsByTagName("formatted_address");
	                  nodename = nlname.item(i);
	            	aldir.add(nodename.getTextContent());
	            	
	                Node node1 = nl1.item(i);
	                nl2 = node1.getChildNodes();

	                Node locationNode = nl2.item(getNodeIndex(nl2, "location"));
	                nl3 = locationNode.getChildNodes();
	                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
	                double lat = Double.parseDouble(latNode.getTextContent());
	                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
	                double lng = Double.parseDouble(lngNode.getTextContent());
	                listGeopoints.add(new LatLng(lat, lng));

	               
	        }
	        }
			return listGeopoints;
	   }
	        private int getNodeIndex(NodeList nl, String nodename) {
	            for(int i = 0 ; i < nl.getLength() ; i++) {
	                if(nl.item(i).getNodeName().equals(nodename))
	                    return i;
	            }
	            return -1;
	        }
	        

}
