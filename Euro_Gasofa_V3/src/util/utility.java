package util;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.androidmobile.JR.R;
import com.androidmobile.bd.BdGas;
import com.androidmobile.model.DatosIni;
import com.androidmobile.model.Favoritos;
import com.androidmobile.model.Gasolinera;
import com.androidmobile.model.ListaFavoritos;
import com.androidmobile.model.ListaGasolineras;

public class utility {
	private static final String HTML_ROOT = "file:///android_asset/www/";
	private static final String JAVASCRIPT = "javascript:";
	@SuppressWarnings("unused")
	private static final String BRC = "()";
	private static final String BRC_OPEN = "('";
	private static final String BRC_CLOSE = "')";
	@SuppressWarnings("unused")
	private static final String Q = "?";
	public static final String EMPTY_FAVORI_LIST = "{\"listafavoritos\":[]}";
	public static final String EMPTY_GSOLIN_LIST = "{\"listagasolineras\":[]}";
	public static final String Surl = "http://www.elpreciodelagasolina.com/gasolineras/";
	
	static BufferedReader br;
	static BufferedReader brNew;

	
	public static String getAllRestJSONFavoritos(
			ArrayList<Favoritos> alfavoritos) {
		StringWriter writer = new StringWriter();
		ListaFavoritos listafavoritos = new ListaFavoritos();
		listafavoritos.setListaFavoritos(alfavoritos);
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(writer, listafavoritos);			
		} catch (Exception e) {
			return EMPTY_FAVORI_LIST;
		}
		return writer.toString();
	}


	
	public static String getAllRestJSONGasolinera(
			ArrayList<Gasolinera> algasolinera) {
		StringWriter writer = new StringWriter();
		ListaGasolineras listagasolineras = new ListaGasolineras();
		listagasolineras.setListagasolineras(algasolinera);

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(writer, listagasolineras);
		} catch (Exception e) {

			return EMPTY_GSOLIN_LIST;
		}

		return writer.toString();
	}

	public static void tratamientoDatosGasolinera(final DatosIni datos,final Context mContext)
			 {
		
	  final WebView webview = (WebView)  ((Activity) mContext).findViewById(R.id.mainWebView);
		class taskgasNew extends AsyncTask<Void, Void, String> {
			private ProgressDialog pd;
			boolean excep = true;
			
			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(mContext);
				//pd.setTitle("Procesando Datos...");
				pd.setMessage("Espere...                    ");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}

			@Override
			protected String doInBackground(Void... args) {
				 String prov=datos.getProvincia();
			     String mun=datos.getMunicipio();
				 String direc=datos.getDireccion();
				 String num=datos.getNum();
				 String cp=datos.getCp();
			     String comb=datos.getCombustible();;
			
				ArrayList<Gasolinera> alg = new ArrayList<Gasolinera>();
					if(prov.equals("") && !cp.equals(""))
							prov=cp.substring(0, 2);
												
					  try {
						  final String Newurl = "http://geoportal.mityc.es/hidrocarburos/eess/searchAddress.do";
						  	 
						  	          HttpClient httpclient = new DefaultHttpClient();
						  	         	/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
						  	          HttpPost httppost = new HttpPost(Newurl);
						  	      //  httppost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
						  	    //httppost.addHeader( "X-Requested-With", "XMLHttpRequest");
						  	   httppost.addHeader("Accept-Language", "es");
						  	        
						  	/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
						  	         // AÑADIR PARAMETROS
						  	    //http://geoportal.mityc.es/hidrocarburos/eess/searchAddress.do?nomProvincia=28&nomMunicipio=mostoles&tipoCarburante=4&rotulo=&tipoVenta=false&nombreVia=&numVia=&codPostal=&economicas=false&tipoBusqueda=0&ordenacion=P&posicion=0
						  	          List<NameValuePair> params = new ArrayList<NameValuePair>();
						  	          params.add(new BasicNameValuePair("codPostal",cp));
						  	          params.add(new BasicNameValuePair("economicas","false"));
						  	          params.add(new BasicNameValuePair("nomMunicipio",mun));
						  	          params.add(new BasicNameValuePair("nomProvincia",prov));
						  	          params.add(new BasicNameValuePair("nombreVia",direc));
						  	         params.add(new BasicNameValuePair("numVia",num));
						  	         params.add(new BasicNameValuePair("ordenacion","P"));
						  	         params.add(new BasicNameValuePair("rotulo",""));
						  	         params.add(new BasicNameValuePair("tipoBusqueda","0"));
						  	         params.add(new BasicNameValuePair("tipoCarburante",comb));
						  	         params.add(new BasicNameValuePair("tipoVenta","true"));
						  	         params.add(new BasicNameValuePair("posicion","0"));
						  	       
						  	        /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
						  	  httppost.setEntity(new UrlEncodedFormEntity(params));
						  	 
						  	                  /*Finalmente ejecutamos enviando la info al server*/
						  	          HttpResponse resp = httpclient.execute(httppost);
						  	      				  	         
						  	          
						  	      
						  	 
						  	      
						  	         
						  	        InputStream in = resp.getEntity().getContent();
						  	        
						  	      brNew = new BufferedReader(new InputStreamReader(in));
									String text;
									String rdo = "";
									String nombre = "-";
									String direccion = "-";
									String localidad = "-";
									String provincia = "-";
									String gasolina95 = "-";
									String gasolina98 = "-";
									String gasoleo = "-";
									@SuppressWarnings("unused")
									String margen="-";
									String fecha="-";
									@SuppressWarnings("unused")
									String venta="-";
									@SuppressWarnings("unused")
									String rem="-";
									String horario="-";
									String UPV="-";
									String combustible;
									while ((text = brNew.readLine()) != null) {
										rdo += text.trim();
									}
									brNew.close();	
									int pos = 0;
									pos = rdo.indexOf("<td class=\"tdMediumBorderLeftTable\">")+("<td class=\"tdMediumBorderLeftTable\">").length();
									int pos2;
									while (pos != -1) {//añadido 13/09/2013
																			
										pos2 = rdo.indexOf("</td>", pos +4 );
										provincia = (rdo.substring(pos , pos2 ));
										provincia = provincia.replaceAll("\"", "").replaceAll("\'", "");
										
										rdo=rdo.substring(pos2, rdo.length());			
										pos = rdo.indexOf("<td class=\"tdMedium\">", 5);
										pos = rdo.indexOf(">", pos + 5) + 1;
										pos2 = rdo.indexOf("</td>", pos +4);
										localidad = rdo.substring(pos, pos2);
										localidad = localidad.replaceAll("\"", "").replaceAll("\'", "");
										
										rdo=rdo.substring(pos2, rdo.length());		
										pos = rdo.indexOf("<td class=\"tdXLong\">",  5);
										pos2 = rdo.indexOf("</td>", pos +4);
										direccion = rdo.substring(pos+("<td class=\"tdXLong\">").length(), pos2);
										direccion = direccion.replaceAll("\"", "").replaceAll("\'", "");
										
										rdo=rdo.substring(pos2, rdo.length());		
										pos = rdo.indexOf("<td class=\"tdXShort\">",  5);
										pos2 = rdo.indexOf("</td>", pos +4);
										margen = rdo.substring(pos+("<td class=\"tdXShort\">").length(), pos2);
																			
										rdo=rdo.substring(pos2, rdo.length());		
										pos = rdo.indexOf("<td class=\"tdShort\">",  5);
										pos2 = rdo.indexOf("</td>", pos +4);
										fecha = rdo.substring(pos+("<td class=\"tdShort\">").length(), pos2);
										
										rdo=rdo.substring(pos2, rdo.length());		
										pos = rdo.indexOf("<td class=\"tdXShort\">",  5);
										pos2 = rdo.indexOf("</td>", pos +4);
										combustible = rdo.substring(pos+("<td class=\"tdXShort\">").length(), pos2);
										if (comb.equals("1"))
											gasolina95=combustible;
										if (comb.equals("3"))
											gasolina98=combustible;
										if (comb.equals("4"))
											gasoleo=combustible;
										
										rdo=rdo.substring(pos2, rdo.length());		
										pos = rdo.indexOf("<td class=\"tdMedium\">", 2);
										pos2 = rdo.indexOf("</td>", pos +4);
										nombre = rdo.substring(pos+("<td class=\"tdMedium\">").length(), pos2);
										nombre = nombre.replaceAll("\"", "").replaceAll("\'", "");
										
										rdo=rdo.substring(pos2, rdo.length());		
										pos = rdo.indexOf("<td class=\"tdXShort\">", 5);
										pos2 = rdo.indexOf("</td>", pos +4);
										venta = rdo.substring(pos+("<td class=\"tdXShort\">").length(), pos2);
										
										rdo=rdo.substring(pos2, rdo.length());		
										pos = rdo.indexOf("<td class=\"tdXShort\">",5);
										pos2 = rdo.indexOf("</td>", pos +4);
										rem = rdo.substring(pos+("<td class=\"tdXShort\">").length(), pos2);
										
										rdo=rdo.substring(pos2, rdo.length());		
										pos = rdo.indexOf("<td class=\"tdMedium\">",  5);
										//pos = rdo.indexOf(">", pos + 5) + 1;
										pos2 = rdo.indexOf("</td>", pos +4);
										horario = rdo.substring(pos+("<td class=\"tdMedium\">").length(), pos2);
										
										rdo=rdo.substring(pos2, rdo.length());		
										pos = rdo.indexOf("onClick=\"centrar",  5);
										//pos = rdo.indexOf(">", pos + 5) + 1;
										pos2 = rdo.indexOf("</img>", pos +1)-6;
										UPV = rdo.substring(pos+("onClick=\"centrarr").length(), pos2);
										
										
									
										
										Gasolinera g = new Gasolinera(UPV.trim(),fecha.trim(), nombre.trim(),
												direccion.trim(), localidad.trim(), provincia.trim(),horario.trim(), gasolina95.trim(),
												gasolina98.trim(), gasoleo.trim());
										alg.add(g);
										
										
											rdo=rdo.substring(pos2, rdo.length());													
											pos = rdo.indexOf("<td class=\"tdMediumBorderLeftTable\">",4 )+("<td class=\"tdMediumBorderLeftTable\">").length();
											
											
									}
						  	     
						  	 
						  	  
					  } catch (IndexOutOfBoundsException ex) {
					ex.printStackTrace();
						
					  } catch (Exception ex) {
					excep = false;
					Log.i("utility", ex.getMessage());
					webview.loadUrl(JAVASCRIPT + "showToast" + BRC_OPEN
							+ ex.getMessage() + BRC_CLOSE);
					webview.loadUrl(HTML_ROOT + "indexGas.html");
				}
			 String json = EMPTY_GSOLIN_LIST ;
				try{
				BdGas bdgas = new BdGas(mContext);
				bdgas.writerBdGas(alg);
				ArrayList<Gasolinera> algOrder = bdgas.readBdGas(null);
				json= getAllRestJSONGasolinera(algOrder);
				if (json.equals(EMPTY_GSOLIN_LIST) && excep) {
					webview.loadUrl(JAVASCRIPT + "showToast" + BRC_OPEN
							+ "Sin Resultados" + BRC_CLOSE);
					webview.loadUrl(HTML_ROOT + "indexGas.html");
				}else
				return json;
				}catch(Exception e){
					webview.loadUrl(JAVASCRIPT + "showToast" + BRC_OPEN
							+ "Problema de conexion al Servidor" + BRC_CLOSE);
					webview.loadUrl(HTML_ROOT + "indexGas.html");
				}
				return json;
			}

			@Override
			protected void onPostExecute(String json) {
				try{
				Log.i("onpostexecute", json);
				
				webview.loadUrl(JAVASCRIPT + "setRestList" + BRC_OPEN + json
						+ BRC_CLOSE);
				}catch(Exception e){
					Log.i("err", e.getMessage());
				}	
				
				pd.dismiss();

			}
		}

		// return null;
		new taskgasNew().execute();
	}




	
	public static void ordenadoDatosGasolinera(final String order,
			final Context mContext, final WebView webview)
			throws InterruptedException, ExecutionException {

		class taskgas2 extends AsyncTask<Void, Void, String> {
			private ProgressDialog pd;

			@Override
			protected void onPreExecute() {
			
				pd = new ProgressDialog(mContext);
				//pd.setTitle("Procesando Datos...");
				pd.setMessage("Espere...                    ");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			
			}

			@Override
			protected String doInBackground(Void... args) {
				ArrayList<Gasolinera> algOrder = null;
				BdGas bdgas = new BdGas(mContext);

				algOrder = bdgas.readBdGas(order);
				String json = utility.getAllRestJSONGasolinera(algOrder);
				if (json.equals(EMPTY_GSOLIN_LIST)){
					webview.loadUrl(JAVASCRIPT + "showToast" + BRC_OPEN
							+ "Sin Resultados" + BRC_CLOSE);
					//webview.loadUrl(HTML_ROOT + "indexGas.html");
				}
			
				return json;

			}

			@Override
			protected void onPostExecute(String json) {
				Log.i("onpostexecute", json);
				
				webview.loadUrl(JAVASCRIPT + "setRestList" + BRC_OPEN + json
						+ BRC_CLOSE);
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				pd.dismiss();
			}
		}

		// return null;
		new taskgas2().execute();

	}

	public static void retardo(final WebView webview, final int retardo,
			final ProgressBar barraProgreso) throws InterruptedException,
			ExecutionException {

		class taskretardo extends AsyncTask<Void, Void, Void> {

			int progreso;

			// Método que se ejecutará antes de la tarea en segundo plano,
			// normalmente utilizado para iniciar el entorno gráfico
			protected void onPreExecute() {
				barraProgreso.setProgress(0);// Ponemos la barra de progreso a 0
				barraProgreso.setMax(100);// El máximo de la barra de progreso
											// son 60, de los 60 segundo que va
											// a durar la tarea en segundo
											// plano.
				barraProgreso.setVisibility(View.VISIBLE);
			}

			// Este método se ejecutará después y será el que ejecute el código
			// en segundo plano
			@Override
			protected Void doInBackground(Void... params) {
				for (progreso = 1; progreso <= 100; progreso++) {// Creamos un
																	// for de 1
																	// a 60 que
																	// irá
																	// contando
																	// los
																	// segundos.

					try {
						Thread.sleep(retardo);// Esto lo que hace es ralentizar
												// este proceso un segundo (el
												// tiempo que se pone entre
												// paréntesis es en
												// milisegundos) tiene que ir
												// entre try y catch
					} catch (InterruptedException e) {
					}

					publishProgress();// Actualizamos el progreso, es decir al
										// llamar a este proceso en realidad
										// estamos llamamos al método
										// onProgresssUpdate()
				}

				return null;// Al llegar aquí, no devolvemos nada y acaba la
							// tarea en segundo plano y se llama al método
							// onPostExecute().
			}

			protected void onProgressUpdate(Void... values) {

				barraProgreso.setProgress(progreso);// Actualizamos la barra de
													// progreso con los segundos
													// que vayan.
			}

			protected void onPostExecute(Void result) {// A este método se le
														// llama cada vez que
														// termine la tarea en
														// segundo plano.
				barraProgreso.setVisibility(View.GONE);
				webview.loadUrl(HTML_ROOT + "indexGas.html");
			}
		}
		new taskretardo().execute();

	}
}
	