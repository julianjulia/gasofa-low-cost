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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.androidmobile.JR.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GMapV2GasProx {
	static GoogleMap mapa;
	final Double variable=0.08;  // recordar 0.01 aprox 1km   es decir 16km
	private static Context mContext;	
	private double lat;
	private double lon;
	static LatLng  UPV ;
	String url;  	
	static String dcomb;
	static ArrayList<String> alname;
	static ArrayList<Double> alprec;
	static Double pMax=(double) 0;
	static Double pMin=(double) 0;
	Double pm=(double) 0;
	static Double V1=(double) 0;
	static Double V2=(double) 0;
	static ArrayList<LatLng> lt;
	   public GMapV2GasProx(Context mContext,LatLng UPV , String comb){
		   this.mContext=mContext;
		    this.UPV=UPV;
		    lat=UPV.latitude;
			lon=UPV.longitude;
			if(comb.equals("1"))
				this.dcomb="gasolina95: ";
			if(comb.equals("3"))
				this.dcomb="gasolina98: ";
			if(comb.equals("4"))
				this.dcomb="gasoleo: ";
		  url="https://maps.googleapis.com/maps/api/place/textsearch/"
					+"xml?location="+lat+","+lon+"&query=gasolinera&radius=2000&sensor=true&key=AIzaSyBbew32IyIjmhVtRqOuw1VDVeESwyJUMjk";									
		  
		  url="http://geoportal.mityc.es/hidrocarburos/eess/queryPopUp.do?urlValor=http://geoportal.mityc.es/cgi-bin/mapserv?SERVICE=WFS&VERSION=1.0.0&REQUEST=GetFeature&TYPENAME=estaciones_servicio_brief&BBOX="+(lon-variable)+","+(lat-variable)+","+(lon+variable)+","+(lat+variable)+"&tipoCarburante="+comb+"&tipoBusqueda=0";
		  
		  SupportMapFragment fm = (SupportMapFragment) ((FragmentActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.map);
			 
	         // Getting GoogleMap object from the fragment
	        mapa = fm.getMap();
		  new taskgasP().execute();
	   }
	   public GMapV2GasProx(Context mContext){
		   this.mContext=mContext;
	   }
	   class taskgasP extends AsyncTask<Void, Void, Void> implements OnCancelListener{
			private ProgressDialog pd;
			Document doc;
			
			 @Override
			protected void onPreExecute() {
				
				pd = new ProgressDialog(mContext);
				//pd.setTitle("Procesando Datos...");
				pd.setMessage("Espere...                    ");
				pd.setCancelable(true);
				pd.setOnCancelListener(this);
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
				
				 lt= getDirection(doc);
				 mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV,14));
				for (int i = 0;i<lt.size();i++){
				if(((alprec.get(i)>pMin) && (alprec.get(i) < V1)) || (alprec.get(i)==pMin) )
	             mapa.addMarker(new MarkerOptions().position(lt.get(i)).title(alname.get(i)).snippet(dcomb+" "+alprec.get(i))
	            		 .icon(BitmapDescriptorFactory.fromResource(R.drawable.gazstation_v)));
				else if((alprec.get(i)>=V1) && (alprec.get(i) < V2))
		             mapa.addMarker(new MarkerOptions().position(lt.get(i)).title(alname.get(i)).snippet(dcomb+" "+alprec.get(i))
		            		 .icon(BitmapDescriptorFactory.fromResource(R.drawable.gazstation_m)));
				if(((alprec.get(i) >=V2) && (alprec.get(i) < pMax)) ||(alprec.get(i) == pMax) )
		             mapa.addMarker(new MarkerOptions().position(lt.get(i)).title(alname.get(i)).snippet(dcomb+" "+alprec.get(i))
		            		 .icon(BitmapDescriptorFactory.fromResource(R.drawable.gazstation_r)));
				}
			}

			@Override
			public void onCancel(DialogInterface dialog) {
				if(this.getStatus()==AsyncTask.Status.RUNNING){
					this.cancel(true);   
					//httpPost.abort();
					dialog.dismiss();
					  
					   Toast.makeText(mContext, "Operacion cancelada!!!!",
					                Toast.LENGTH_SHORT).show();
					}
				
			}
	   }
	  
	   public ArrayList<LatLng> getDirection (Document doc) {
		    alname = new ArrayList<String>();
		    alprec = new ArrayList<Double>();
	        NodeList nl1, nl2;
	        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
	        nl1 = doc.getElementsByTagName("elemento");
	        if (nl1.getLength() > 0) {
	            for (int i = 0; i < nl1.getLength(); i++) {
	            	 // obtener el nombre
	            	
	            	
	                Node node1 = nl1.item(i);
	                nl2 = node1.getChildNodes();
                
	                
	                Node latNode = nl2.item(getNodeIndex(nl2, "y"));
	                double lat = Double.parseDouble(latNode.getTextContent());
	                Node lngNode = nl2.item(getNodeIndex(nl2, "x"));
	                double lng = Double.parseDouble(lngNode.getTextContent());
	                listGeopoints.add(new LatLng(lat, lng));
	                
	                Node rotNode = nl2.item(getNodeIndex(nl2, "rotulo"));
	                String rotulo = rotNode.getTextContent();
	                alname.add(rotulo);
	                
	                Node pvNode = nl2.item(getNodeIndex(nl2, "precio"));
	                Double precio = Double.parseDouble(pvNode.getTextContent().replace(",", "."));
	                calcularPrecios(precio);	     
	                alprec.add(precio);
	                        
	        }
	        }
	        calcularPrecioMedio();
			return listGeopoints;
	   }
	        private void calcularPrecioMedio() {
		
	        pm=pm/(alprec.size());
		
	        V1=(pMin+pm)/2;
	        V1=(pm+V1)/2;
	        V2=(pMax+pm)/2;
	        V2=(pm+V2)/2;
	        
	}
			private void calcularPrecios(Double precio) {
				
	        if(precio>pMax)
	        	pMax=precio;
	        if(precio<pMin || pMin==0)
	        	pMin=precio;
	        pm=pm+precio;	
		
	        
	}
			private int getNodeIndex(NodeList nl, String nodename) {
	            for(int i = 0 ; i < nl.getLength() ; i++) {
	                if(nl.item(i).getNodeName().equals(nodename))
	                    return i;
	            }
	            return -1;
	        }
	        
			public static void LoadLogosGas(){
				mapa.clear();
				int icon = 0;
				 mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV,14));
					for (int i = 0;i<lt.size();i++){
						icon= buscarIcono(alname.get(i).toLowerCase());
					if(((alprec.get(i)>pMin) && (alprec.get(i) < V1)) || (alprec.get(i)==pMin) ){
						 mapa.addMarker(new MarkerOptions().position(lt.get(i)).title(alname.get(i)).snippet(dcomb+" "+alprec.get(i))
		            		 .icon(BitmapDescriptorFactory.fromResource(icon)));
					}else if((alprec.get(i)>=V1) && (alprec.get(i) < V2)){
						icon= buscarIcono(alname.get(i).toLowerCase());
			             mapa.addMarker(new MarkerOptions().position(lt.get(i)).title(alname.get(i)).snippet(dcomb+" "+alprec.get(i))
			            		 .icon(BitmapDescriptorFactory.fromResource(icon)));
					}else if(((alprec.get(i) >=V2) && (alprec.get(i) < pMax)) ||(alprec.get(i) == pMax) ){
						 mapa.addMarker(new MarkerOptions().position(lt.get(i)).title(alname.get(i)).snippet(dcomb+" "+alprec.get(i))
			            		 .icon(BitmapDescriptorFactory.fromResource(icon)));
					}
					}	
				
			}
			public static int buscarIcono(String valor) {
				if(valor.indexOf("alcampo")!=-1)
					return getDrawableByName("alcampo");
				if(valor.indexOf("ballenoil")!=-1)
					return getDrawableByName("ballenoil");
				if(valor.indexOf("bp")!=-1)
					return getDrawableByName("bp");
				if(valor.indexOf("carrefour")!=-1)
					return getDrawableByName("carrefur");
				if(valor.indexOf("cepsa")!=-1)
					return getDrawableByName("cepsa");
				if(valor.indexOf("campsa")!=-1)
					return getDrawableByName("repsol");
				if(valor.indexOf("galp")!=-1)
					return getDrawableByName("galp");
				if(valor.indexOf("meroil")!=-1)
					return getDrawableByName("meroil");
				if(valor.indexOf("repsol")!=-1)
					return getDrawableByName("repsol");
				if(valor.indexOf("shell")!=-1)
					return getDrawableByName("shell");
				if(valor.indexOf("makro")!=-1)
					return getDrawableByName("makro");
				if(valor.indexOf("eroski")!=-1)
					return getDrawableByName("eroski");	
				
			 return getDrawableByName("gasolinera");
				
				
			}
			public static void LoadColorGas() {
				
				mapa.clear();
				
				 mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV,14));
				for (int i = 0;i<lt.size();i++){
				if(((alprec.get(i)>pMin) && (alprec.get(i) < V1)) || (alprec.get(i)==pMin) )
	             mapa.addMarker(new MarkerOptions().position(lt.get(i)).title(alname.get(i)).snippet(dcomb+" "+alprec.get(i))
	            		 .icon(BitmapDescriptorFactory.fromResource(R.drawable.gazstation_v)));
				else if((alprec.get(i)>=V1) && (alprec.get(i) < V2))
		             mapa.addMarker(new MarkerOptions().position(lt.get(i)).title(alname.get(i)).snippet(dcomb+" "+alprec.get(i))
		            		 .icon(BitmapDescriptorFactory.fromResource(R.drawable.gazstation_m)));
				if(((alprec.get(i) >=V2) && (alprec.get(i) < pMax)) ||(alprec.get(i) == pMax) )
		             mapa.addMarker(new MarkerOptions().position(lt.get(i)).title(alname.get(i)).snippet(dcomb+" "+alprec.get(i))
		            		 .icon(BitmapDescriptorFactory.fromResource(R.drawable.gazstation_r)));
				}
			}
			 public static int getDrawableByName( String name ) {
			        int drawableResource = mContext.getResources().getIdentifier(
			                        name,
			                        "drawable",
			                        mContext.getPackageName());
			        if ( drawableResource == 0 ) {
			            throw new RuntimeException("Can't find drawable with name: " + name );
			        }
			        return drawableResource;
			    }
}
