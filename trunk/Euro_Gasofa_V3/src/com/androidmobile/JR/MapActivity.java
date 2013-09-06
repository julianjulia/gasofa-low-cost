package com.androidmobile.JR;

import java.io.IOException;

import java.util.List;


import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.androidmobile.map.GMapV2Direction;
import com.androidmobile.map.GMapV2GasProx;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapActivity extends FragmentActivity  implements
		OnMapClickListener {
	
	private  LatLng UPV = new LatLng(39.481106, -0.340987);
	LatLng UPV2;
	private String valor="madrid";
	private GoogleMap mapa;
	List<Address> addresses ;
	Bundle bundle;
	Context mContext;
	String gasprox;
	String nombre;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);
	
		mContext=this;
		
			
		 int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		 
	        // Showing status
	        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
	 
	            int requestCode = 10;
	            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
	            dialog.show();
	 
	        }else { // Google Play Services are available
	 
	            // Getting reference to the SupportMapFragment of activity_main.xml
	            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	 
	            // Getting GoogleMap object from the fragment
	            mapa = fm.getMap();
	     
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		//mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV, 15));

		mapa.setMyLocationEnabled(true);

		mapa.getUiSettings().setZoomControlsEnabled(false);

		mapa.getUiSettings().setCompassEnabled(true);
		
		mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
		    public boolean onMarkerClick(Marker marker) {
		        
		            UPV2=marker.getPosition();
		           		 
		        return false;
		    }

			
		});
	       
		mapa.setOnMapClickListener(this);
		
		  class taskgas extends AsyncTask<Void, Void, Void> {
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
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
				}

				@Override
				protected void onPostExecute(Void json) {
					pd.dismiss();
					animateCamera_N(null);
					
					}
				}
		  bundle = getIntent().getExtras();
			gasprox= bundle.getString("gas");
			 if(gasprox != null && gasprox.equals("falso")){
								
		  new taskgas().execute();
		
			 }else{
		// mapa.moveCamera(CameraUpdateFactory.zoomTo(12));
		try {
			irPosicion();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 }	
			
	}
	 }
	public void leyenda(View view) {
		Intent intent = new Intent(MapActivity.this, LeyendaActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("gas","falso");
		intent.putExtras(bundle);
		startActivity(intent);
	}

	
	public void moveCamera(View view) {

		mapa.moveCamera(CameraUpdateFactory.newLatLng(UPV));
		
	}

	public void animateCamera_N(View view) {
		
		ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.style.miestilo);
		
		final CharSequence[] items = {"Ubicacion Actual", "Introducir Direccion"};
		 
		AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
		builder.setTitle("Ubicacion");
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	if(items[item].equals("Ubicacion Actual")){
		    	try {
		    		 if(gasprox != null && gasprox.equals("falso")){
		    			 try{
		    			UPV= new LatLng(mapa.getMyLocation().getLatitude(),mapa.getMyLocation().getLongitude());
		    			
		    			 resulgascer("Mi ubicacion");
		    			 }catch(Exception e){
		    				 Toast.makeText(getApplicationContext(),"No se ha encontrado Ubicacion ", Toast.LENGTH_LONG).show();
		    			 }
		    		 }else
					visualizar(null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	}else{	
		    	consultaDireccion();
		    	
		    	}
		        //Toast toast = Toast.makeText(getApplicationContext(), "Haz elegido la opcion: " + items[item] , Toast.LENGTH_SHORT);
		        //toast.show();
		        dialog.cancel();
		    }
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public  void consultaDireccion(){
		Editable value = null;
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Introduzca direccion");
		alert.setMessage("Direccion-Poblacion");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		   Editable value = input.getText();
		  // Do something with value!
		   try {
			visualizar(value.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block ''''
			Toast.makeText(getApplicationContext(),"No se ha encontrado Ubicacion ", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();	
			 
	}

	public void animateCamera_L(View view) {
	
		 if (mapa.getMyLocation() != null){
			 
			   
				       
	          UPV= new LatLng(mapa.getMyLocation().getLatitude(),mapa.getMyLocation().getLongitude());

	          mapa.addPolyline(new PolylineOptions().add(UPV,UPV2).width(4).color(Color.BLUE));
	    	  
	    	  mapa.addMarker(new MarkerOptions().position(UPV).title("Ubicacion").snippet(UPV.latitude+" -" +UPV.longitude)
	    			  .icon(BitmapDescriptorFactory.fromResource(R.drawable.gazstation)));
	    	  mapa.moveCamera(CameraUpdateFactory.zoomTo(12));
	    	  
	    	  Location l1 = new Location("UPV");
			    l1.setLatitude(UPV.latitude);
			    l1.setLongitude(UPV.longitude);
			    Location l2 = new Location("UPV2");
			    l2.setLatitude(UPV2.latitude);
			    l2.setLongitude(UPV2.longitude);
	         	
			    int distance = (int)l1.distanceTo(l2);
			    
			    Toast.makeText(this,distance + " metros", Toast.LENGTH_LONG).show();
		 }else
			 Toast.makeText(this,"No se ha encontrado Ubicacion ", Toast.LENGTH_LONG).show();
		 
	}
			

	@Override
	public void onMapClick(LatLng puntoPulsado) {

		
		//mapa.addMarker(new MarkerOptions().position(puntoPulsado).

		//icon(BitmapDescriptorFactory

		//.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

	}
	public void irPosicion() throws Throwable {
		   
		if (addresses!=null){
			int num=valor.indexOf(" ");
			System.out.println(num);
			valor=valor.substring(num).trim();
		}else{	
		
		bundle = getIntent().getExtras();
		valor= bundle.getString("direccion");
		nombre= bundle.getString("nombre");
		
		}
		Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
		try {
			addresses = geoCoder.getFromLocationName(valor, 5);
			if ( addresses!=null && addresses.size() > 0) {
	        	 UPV2 = new LatLng(addresses.get(0).getLatitude() ,addresses.get(0).getLongitude() );
	            
	        	 mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV2,15));
	         
	        	 mapa.addMarker(new MarkerOptions().position(UPV2).title(nombre).snippet(valor)
	        			 .icon(BitmapDescriptorFactory.fromResource(R.drawable.gazstation)));
	            
	         }else{
	        	 if (valor.equals("")) {
	        			 Toast.makeText(this,"No se ha encontrado Ubicacion ", Toast.LENGTH_LONG).show();
	    	        	finalize();
	        	 }else {
	        	 irPosicion();
	        	 }
	         	
		}
			} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(this,"Error de Red.... ", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			finish();
		}         
}

	
	public void visualizar(String dir) throws IOException{
		
		if(dir != null && dir!=""){
			showresultados(dir);
				}else{
		
		if (mapa.getMyLocation() != null){
			
			 UPV= new LatLng(mapa.getMyLocation().getLatitude(),mapa.getMyLocation().getLongitude());
		
		GMapV2Direction md = new GMapV2Direction(this);		
		
			md.getDocument(mapa,UPV, UPV2, GMapV2Direction.MODE_DRIVING);
		 mapa.addMarker(new MarkerOptions().position(UPV).title("Ubicacion").snippet(UPV.latitude+"  " +UPV.longitude)
 			  .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
		}else{			 
			 Toast.makeText(getApplicationContext(),"No se ha encontrado Ubicacion ", Toast.LENGTH_LONG).show();
		}
	}
	}
	
	
	public void showresultados(String dir) throws IOException{
		Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
		
		addresses = geoCoder.getFromLocationName(dir, 5);
		if ( addresses!=null && addresses.size() > 0) {
			
			final CharSequence[] itemsdir = new CharSequence[addresses.size()];
			for(int i=0;i<addresses.size();i++){
				String call="";
				if(addresses.get(i).getThoroughfare()== null)
					call="";
					else
						call=addresses.get(i).getThoroughfare();
				String d=call+" "+addresses.get(i).getFeatureName()+" " +addresses.get(i).getLocality()+" "+addresses.get(i).getSubAdminArea();
										
				itemsdir[i]=d;
			}
			ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.style.miestilo);
			AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
			builder.setTitle("Resultados");
			builder.setItems(itemsdir, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	paintResult(item);
			        dialog.cancel();
			        
			    }
			});
			
			AlertDialog alert = builder.create();
			alert.show();
			
					
			
        	
         }else{
        	 Toast.makeText(getApplicationContext(),"No se ha encontrado Ubicacion "+ dir, Toast.LENGTH_LONG).show();
         }
	}
	
		public void paintResult(int item){
			 UPV = new LatLng(addresses.get(item).getLatitude() ,addresses.get(item).getLongitude() );
			 String call="";
	 			if(addresses.get(item).getThoroughfare()== null)
					call="";
					else
	 			call=addresses.get(item).getThoroughfare();
				String d=call+" "+addresses.get(item).getFeatureName()+" " +addresses.get(item).getLocality()+" "+addresses.get(item).getSubAdminArea();
						
			 if(gasprox != null && gasprox.equals("falso")){
				 resulgascer(d);
			 }else{
				 
        	 GMapV2Direction md = new GMapV2Direction(this);		
     		
 			md.getDocument(mapa,UPV, UPV2, GMapV2Direction.MODE_DRIVING);
 						
 		 mapa.addMarker(new MarkerOptions().position(UPV).title("Ubicacion").snippet(d)
  			  .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
			 }
			 }
		public void resulgascer(String d){
			gasprox="verdadero";
		 try{  				
				
			// UPV= new LatLng(mapa.getMyLocation().getLatitude(),mapa.getMyLocation().getLongitude());
		         mapa.addMarker(new MarkerOptions().position(UPV).title("Ubicacion").snippet(d)
		      			  .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
		         Toast.makeText(mContext,"pulse marcador para informacion o calcular ruta", Toast.LENGTH_LONG).show();
		  new GMapV2GasProx(mContext,mapa,UPV);
		 }catch(Exception e){
			 Toast.makeText(mContext,"No se ha encontrado Ubicacion ", Toast.LENGTH_LONG).show();
			 try {
				finalize();
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }}
}