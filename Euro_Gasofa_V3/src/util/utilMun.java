package util;
import java.io.IOException;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.androidmobile.JR.R;
import com.androidmobile.bd.BdGas;
import com.androidmobile.model.Municipio;
import com.androidmobile.model.Provincia;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;


@TargetApi(Build.VERSION_CODES.FROYO)
@SuppressLint("NewApi")
public class utilMun {

		  	
	ArrayList<String> alname;
	ArrayList<String> aldir;
	
	  
	   
	   public void loadWebMunicipios(final Context mContext,final String pov,final String cod){

		   final String  url="http://geoportal.mityc.es/hidrocarburos/eess/municipios.do";
		   
		 
		   
	   class taskgasP extends AsyncTask<Void, Void, Void> {
			private ProgressDialog pd;
			Document doc;
			
			
			
				 
			 @Override
			protected void onPreExecute() {
				
				pd = new ProgressDialog(mContext);
				//pd.setTitle("Procesando Datos...");
				pd.setMessage("Actualizando Municipios..."+pov);
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
				
			}

			@Override
			protected Void doInBackground(Void... args) {
				try {
					
				       HttpClient httpclient = new DefaultHttpClient();
		  	         	/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
		  	          HttpPost httppost = new HttpPost(url);
		  	        httppost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		  	      httppost.addHeader( "X-Requested-With", "XMLHttpRequest");
		  	
		  	        
		  	/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
		  	         // AÑADIR PARAMETROS
		  	          List<NameValuePair> params = new ArrayList<NameValuePair>();
		  	          params.add(new BasicNameValuePair("idProvincia",cod));
		  	          params.add(new BasicNameValuePair("tipoBusqueda","0"));
		  	            /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
		  	          httppost.setEntity(new UrlEncodedFormEntity(params));
		  	        /*Finalmente ejecutamos enviando la info al server*/
		  	          HttpResponse resp = httpclient.execute(httppost);
					  InputStream in = resp.getEntity().getContent();
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
				
				ArrayList<Municipio> lt= getMunicipio(doc,cod);
				BdGas bd=new BdGas(mContext);
				bd.writerBdMunicipio(lt);
			
			}
	   }
	   new taskgasP().execute();
	   }
	  
	   public ArrayList<Municipio> getMunicipio (Document doc,String cod) {
	       NodeList nl1,nl3;
	        ArrayList<Municipio> listMun = new ArrayList<Municipio>();
	         nl1 = doc.getElementsByTagName("provincia");
	         if (nl1.getLength() > 0) {
	             for (int i = 0; i < nl1.getLength(); i++) {
	                 Node node1 = nl1.item(i);
	                 nl3 = node1.getChildNodes();       
	                 Node nomNode = nl3.item(getNodeIndex(nl3, "m"));
	                 String nombre_municipio = nomNode.getTextContent();
	                 listMun.add(new Municipio(cod , nombre_municipio));
	             }
	          
		}
			return listMun;
	   }
	        private int getNodeIndex(NodeList nl, String nodename) {
	            for(int i = 0 ; i < nl.getLength() ; i++) {
	                if(nl.item(i).getNodeName().equals(nodename))
	                    return i;
	            }
	            return -1;
	        }
	   
}