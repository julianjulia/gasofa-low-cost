package com.adroidmobile.map;

import java.io.BufferedReader;


import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.map.ObjectMapper;



import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.androidmobile.bd.BdGas;
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

	public static void tratamientoDatosGasolinera(final String poblac,
			final Context mContext, final WebView webview)
			throws InterruptedException, ExecutionException {
	
		class taskgas extends AsyncTask<Void, Void, String> {
			private ProgressDialog pd;
			boolean excep = true;
			
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
			protected String doInBackground(Void... args) {
				ArrayList<Gasolinera> alg = new ArrayList<Gasolinera>();
				try {

					URL url = new URL(Surl + poblac);
					URLConnection cnn = url.openConnection();
					br = new BufferedReader(new InputStreamReader(
							cnn.getInputStream()));

					String text;
					String rdo = "";
					String nombre = "";
					String direccion = "";
					String localidad = "";
					String provincia = "";
					String gasolina95 = "";
					String gasolina98 = "";
					String gasoleo = "";

					while ((text = br.readLine()) != null) {
						rdo += text;
					}
					int pos = rdo.indexOf("<td>");
					String distancia;
					int pos2;
					while (pos != -1) {
						pos2 = rdo.indexOf("</td>", pos + 5);
						distancia = (rdo.substring(pos + 4, pos2 - 3));

						pos = rdo.indexOf("<td>", pos2 + 5);

						pos2 = rdo.indexOf("</td>", pos + 5);
						nombre = rdo.substring(pos + 4, pos2);

						pos = rdo.indexOf("<td>", pos2 + 5);
						pos = rdo.indexOf(">", pos + 5) + 1;
						pos2 = rdo.indexOf("</a>", pos);
						direccion = rdo.substring(pos, pos2);
						pos2 = rdo.indexOf("</td>", pos2 + 1);

						pos = rdo.indexOf("<td>", pos2 + 5);
						pos = rdo.indexOf(">", pos + 5) + 1;
						pos2 = rdo.indexOf("</a>", pos);
						localidad = rdo.substring(pos, pos2);
						pos2 = rdo.indexOf("</td>", pos2 + 1);

						pos = rdo.indexOf("<td>", pos2 + 5);
						pos = rdo.indexOf(">", pos + 5) + 1;
						pos2 = rdo.indexOf("</a>", pos);
						provincia = rdo.substring(pos, pos2);
						pos2 = rdo.indexOf("</td>", pos2 + 1);

						pos = rdo.indexOf("<td>", pos2 + 5);
						pos2 = rdo.indexOf("</td>", pos + 5);
						gasolina95 = rdo.substring(pos + 4, pos2);

						pos = rdo.indexOf("<td>", pos2 + 5);
						pos2 = rdo.indexOf("</td>", pos + 5);
						gasolina98 = rdo.substring(pos + 4, pos2);

						pos = rdo.indexOf("<td>", pos2 + 5);
						pos2 = rdo.indexOf("</td>", pos + 5);
						gasoleo = rdo.substring(pos + 4, pos2);

						pos = rdo.indexOf("<td>", pos2 + 5);
						Double distan = Double.valueOf(distancia.replace(",",
								"."));
						Gasolinera g = new Gasolinera(distan, nombre,
								direccion, localidad, provincia, gasolina95,
								gasolina98, gasoleo);
						alg.add(g);
					}

				} catch (Exception ex) {
					excep = false;
					Log.i("utility", ex.getMessage());
					webview.loadUrl(JAVASCRIPT + "showToast" + BRC_OPEN
							+ ex.getMessage() + BRC_CLOSE);
					webview.loadUrl(HTML_ROOT + "indexGas.html");
				}
				BdGas bdgas = new BdGas(mContext);
				bdgas.writerBdGas(alg);
				ArrayList<Gasolinera> algOrder = bdgas.readBdGas(null);
				String json = getAllRestJSONGasolinera(algOrder);
				if (json.equals(EMPTY_GSOLIN_LIST) && excep) {
					webview.loadUrl(JAVASCRIPT + "showToast" + BRC_OPEN
							+ "Sin Resultados" + BRC_CLOSE);
					webview.loadUrl(HTML_ROOT + "indexGas.html");
				}
				return json;

			}

			@Override
			protected void onPostExecute(String json) {
				Log.i("onpostexecute", json);
				
				webview.loadUrl(JAVASCRIPT + "setRestList" + BRC_OPEN + json
						+ BRC_CLOSE);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pd.dismiss();

			}
		}

		// return null;
		new taskgas().execute();

		// Log.i("resultado-utility",new taskgas().get());

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
				pd.setMessage("Espere...");
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
					Thread.sleep(1000);
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
