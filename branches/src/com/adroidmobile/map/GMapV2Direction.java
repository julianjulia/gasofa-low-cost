package com.adroidmobile.map;

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
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class GMapV2Direction {
	public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";
    Document doc;
    Context mContext;
    public static ArrayList<String> al;
    
    public GMapV2Direction(){
    	
    }
    
    public GMapV2Direction(Context mContext) {
    	this.mContext=mContext;
    }

    public void getDocument(final GoogleMap mapa,LatLng start, LatLng end, String mode) {
    	
     final String url = "http://maps.googleapis.com/maps/api/directions/xml?" 
                + "origin=" + start.latitude + "," + start.longitude  
                + "&destination=" + end.latitude + "," + end.longitude 
                + "&sensor=false&units=metric&mode=driving";
    
       
     al = new ArrayList<String>();       
            
            class taskgas2 extends AsyncTask<Void, Void, Void> {
    			private ProgressDialog pd;

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
    					Log.i("url",url);
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
    				ArrayList<LatLng> directionPoint = getDirection(doc);
    				PolylineOptions rectLine = new PolylineOptions().width(4).color(Color.BLUE);

    				for(int i = 0 ; i < directionPoint.size() ; i++) {          
    				rectLine.add(directionPoint.get(i));
    				}
    				mapa.addPolyline(rectLine);
    				mapa.moveCamera(CameraUpdateFactory.zoomTo(13));
    				Log.i("onpostexecute", "onposexecute");
    				pd.dismiss();
    				Toast.makeText(mContext,getDistanceText(doc) 
    						, Toast.LENGTH_LONG).show();
    			}
    		}

    		// return null;
    		new taskgas2().execute();
            
    		
            
            //HttpResponse response = httpClient.execute(httpPost, localContext);
           
            
            
       
        
    }

    public String getDurationText (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("duration");
        Node node1 = nl1.item(nl1.getLength()-1);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
        Log.i("DurationText", node2.getTextContent());
        return node2.getTextContent();
    }

       
    public int getDurationValue (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("duration");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
        Log.i("DurationValue", node2.getTextContent());
        return Integer.parseInt(node2.getTextContent());
    }

    public String getDistanceText (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("distance");
        Node node1 = nl1.item(nl1.getLength()-1);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
        Log.i("DistanceText", node2.getTextContent());
        return node2.getTextContent();
    }

    public int getDistanceValue (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("distance");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
        Log.i("DistanceValue", node2.getTextContent());
        return Integer.parseInt(node2.getTextContent());
    }

    	
    
    public String getStartAddress (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("start_address");
        Node node1 = nl1.item(0);
        Log.i("StartAddress", node1.getTextContent());
        return node1.getTextContent();
    }

    public String getEndAddress (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("end_address");
        Node node1 = nl1.item(0);
        Log.i("StartAddress", node1.getTextContent());
        return node1.getTextContent();
    }

    public String getCopyRights (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("copyrights");
        Node node1 = nl1.item(0);
        Log.i("CopyRights", node1.getTextContent());
        return node1.getTextContent();
    }

    public ArrayList<LatLng> getDirection (Document doc) {
    	al.add("DISTANCIA: "+getDistanceText(doc));
    	al.add("TIEMPO Aprox: "+getDurationText(doc));
        NodeList nl1, nl2, nl3;
        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
        nl1 = doc.getElementsByTagName("step");
        if (nl1.getLength() > 0) {
            for (int i = 0; i < nl1.getLength(); i++) {
                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                Node locationNode = nl2.item(getNodeIndex(nl2, "start_location"));
                nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));

                locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "points"));
                ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
                for(int j = 0 ; j < arr.size() ; j++) {
                    listGeopoints.add(new LatLng(arr.get(j).latitude, arr.get(j).longitude));
                }

                locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));
                
                latNode= nl2.item(getNodeIndex(nl2, "html_instructions"));
                String inst= latNode.getTextContent().replaceAll("\"", "");
                
                if (i==0){
                	                    
                    al.add(inst);
                
                }else{
                node1 = nl1.item(i-1);
                nl2 = node1.getChildNodes();
                locationNode = nl2.item(getNodeIndex(nl2,"distance"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3,"text"));
                              	
                String dis = latNode.getTextContent();
                
                al.add("En "+dis+" "+inst);
                }
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

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }
}
