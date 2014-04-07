package com.androidmobile.util;
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
import com.androidmobile.bd.BdGas;
import com.androidmobile.model.Municipio;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.ContextThemeWrapper;



public class utilMun {

		  	
	ArrayList<String> alname;
	ArrayList<String> aldir;
	
	String  url;
	   
	   public InputStream  loadWebMunicipios(final Context mContext,final String pov,final String cod){

		  url="http://geoportal.mityc.es/hidrocarburos/eess/municipios.do?idProvincia="+ cod +"&tipoBusqueda=0";
		 @SuppressWarnings("unused")
		// url="http://geoportal.mityc.es/hidrocarburos/eess/municipios.do?idProvincia=04&tipoBusqueda=0";
		 
		   
	   class taskgasP extends AsyncTask<Void, Void, Void> {
			private ProgressDialog pd;
			Document doc;
			ContextThemeWrapper ctw = new ContextThemeWrapper( mContext, R.style.miestilo);
			
			
				 
			 @Override
			protected void onPreExecute() {
				
				pd = new ProgressDialog(ctw);
				//pd.setTitle("Procesando Datos...");
				pd.setMessage("Actualizando Municipios..."+pov);
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
			          ArrayList<Municipio> lt= getMunicipio(doc,cod);
						BdGas bd=new BdGas(mContext);
						bd.writerBdMunicipio(lt);
	    				
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
				
				
			
			}
	   }
	 // new taskgasP().execute();
	   
	   if(cod.equals("01"))
		   return mContext.getResources().openRawResource(R.raw.m01);
	   if(cod.equals("02"))
		   return mContext.getResources().openRawResource(R.raw.m02);
	   if(cod.equals("03"))
		   return mContext.getResources().openRawResource(R.raw.m03);
	   if(cod.equals("04"))
		   return mContext.getResources().openRawResource(R.raw.m04);
	   if(cod.equals("05"))
		   return mContext.getResources().openRawResource(R.raw.m05);
	   if(cod.equals("06"))
		   return mContext.getResources().openRawResource(R.raw.m06);
	   if(cod.equals("07"))
		   return mContext.getResources().openRawResource(R.raw.m07);
	   if(cod.equals("08"))
		   return mContext.getResources().openRawResource(R.raw.m08);
	   if(cod.equals("09"))
		   return mContext.getResources().openRawResource(R.raw.m09);
	   if(cod.equals("10"))
		   return mContext.getResources().openRawResource(R.raw.m10);
	   if(cod.equals("11"))
		   return mContext.getResources().openRawResource(R.raw.m11);
	   if(cod.equals("12"))
		   return mContext.getResources().openRawResource(R.raw.m12);
	   if(cod.equals("13"))
		   return mContext.getResources().openRawResource(R.raw.m13);
	   if(cod.equals("14"))
		   return mContext.getResources().openRawResource(R.raw.m14);
	   if(cod.equals("15"))
		   return mContext.getResources().openRawResource(R.raw.m15);
	   if(cod.equals("16"))
		   return mContext.getResources().openRawResource(R.raw.m16);
	   if(cod.equals("17"))
		   return mContext.getResources().openRawResource(R.raw.m17);
	   if(cod.equals("18"))
		   return mContext.getResources().openRawResource(R.raw.m18);
	   if(cod.equals("19"))
		   return mContext.getResources().openRawResource(R.raw.m19);
	   if(cod.equals("20"))
		   return mContext.getResources().openRawResource(R.raw.m20);
	   if(cod.equals("21"))
		   return mContext.getResources().openRawResource(R.raw.m21);
	   if(cod.equals("22"))
		   return mContext.getResources().openRawResource(R.raw.m22);
	   if(cod.equals("23"))
		   return mContext.getResources().openRawResource(R.raw.m23);
	   if(cod.equals("24"))
		   return mContext.getResources().openRawResource(R.raw.m24);
	   if(cod.equals("25"))
		   return mContext.getResources().openRawResource(R.raw.m25);
	   if(cod.equals("26"))
		   return mContext.getResources().openRawResource(R.raw.m26);
	   if(cod.equals("27"))
		   return mContext.getResources().openRawResource(R.raw.m27);
	   if(cod.equals("28"))
		   return mContext.getResources().openRawResource(R.raw.m28);
	   if(cod.equals("29"))
		   return mContext.getResources().openRawResource(R.raw.m29);
	   if(cod.equals("30"))
		   return mContext.getResources().openRawResource(R.raw.m30);
	   if(cod.equals("31"))
		   return mContext.getResources().openRawResource(R.raw.m31);
	   if(cod.equals("32"))
		   return mContext.getResources().openRawResource(R.raw.m32);
	   if(cod.equals("33"))
		   return mContext.getResources().openRawResource(R.raw.m33);
	   if(cod.equals("34"))
		   return mContext.getResources().openRawResource(R.raw.m34);
	   if(cod.equals("35"))
		   return mContext.getResources().openRawResource(R.raw.m35);
	   if(cod.equals("36"))
		   return mContext.getResources().openRawResource(R.raw.m36);
	   if(cod.equals("37"))
		   return mContext.getResources().openRawResource(R.raw.m37);
	   if(cod.equals("38"))
		   return mContext.getResources().openRawResource(R.raw.m38);
	   if(cod.equals("39"))
		   return mContext.getResources().openRawResource(R.raw.m39);
	   if(cod.equals("40"))
		   return mContext.getResources().openRawResource(R.raw.m40);
	   if(cod.equals("41"))
		   return mContext.getResources().openRawResource(R.raw.m41);
	   if(cod.equals("42"))
		   return mContext.getResources().openRawResource(R.raw.m42);
	   if(cod.equals("43"))
		   return mContext.getResources().openRawResource(R.raw.m43);
	   if(cod.equals("44"))
		   return mContext.getResources().openRawResource(R.raw.m44);
	   if(cod.equals("45"))
		   return mContext.getResources().openRawResource(R.raw.m45);
	   if(cod.equals("46"))
		   return mContext.getResources().openRawResource(R.raw.m46);
	   if(cod.equals("47"))
		   return mContext.getResources().openRawResource(R.raw.m47);
	   if(cod.equals("48"))
		   return mContext.getResources().openRawResource(R.raw.m48);
	   if(cod.equals("49"))
		   return mContext.getResources().openRawResource(R.raw.m49);
	   if(cod.equals("50"))
		   return mContext.getResources().openRawResource(R.raw.m50);
	   if(cod.equals("51"))
		   return mContext.getResources().openRawResource(R.raw.m51);
	   if(cod.equals("52"))
		   return mContext.getResources().openRawResource(R.raw.m52);
	return null;
	   }
	  
	   public ArrayList<Municipio> getMunicipio (Document doc,String cod) {
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