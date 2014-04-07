package com.androidmobile.menulateral;

import java.io.StringWriter;

import java.util.ArrayList;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.androidmobile.JR.MainActivity;
import com.androidmobile.JR.MapActivity;
import com.androidmobile.JR.R;
import com.androidmobile.bd.BdGas;
import com.androidmobile.model.Alerta;
import com.androidmobile.model.ListaAlertas;
import com.androidmobile.service.UpdaterService;
import com.androidmobile.JR.PrefesActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.Toast;


public class SlidingMenuFragment extends Fragment implements ExpandableListView.OnChildClickListener {

	private static final String HTML_ROOT = "file:///android_asset/www/";
	private static final String JAVASCRIPT = "javascript:";
	private static final String BRC_OPEN = "('";
	private static final String Q = "','";
	private static final String BRC_CLOSE = "')";
	public static final String EMPTY_ALERT_LIST = "{\"listaalertas\":[]}";
	String json;
	public static boolean flat_alerta=true;
	private ExpandableListView sectionListView;
  
    private BdGas bd;
    public boolean flat ;
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	//this.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    	//this.prefs.registerOnSharedPreferenceChangeListener(this);
    	//flat_alerta=true;
    	flat = true;
        List<Section> sectionList = createMenu();
               
        View view = inflater.inflate(R.layout.slidingmenu_fragment, container, false);
        this.sectionListView = (ExpandableListView) view.findViewById(R.id.slidingmenu_view);
        this.sectionListView.setGroupIndicator(null);
       
        SectionListAdapter sectionListAdapter = new SectionListAdapter(this.getActivity(), sectionList);
        this.sectionListView.setAdapter(sectionListAdapter);
       
        this.sectionListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
              @Override
              public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
              }
            });
       
        this.sectionListView.setOnChildClickListener(this);
       
        int count = sectionListAdapter.getGroupCount();
        for (int position = 0; position < count; position++) {
            this.sectionListView.expandGroup(position);
        }
       
        return view;
    }

    private List<Section> createMenu() {
        List<Section> sectionList = new ArrayList<Section>();

        Section oBusquedaSection = new Section("Busqueda");
        //oBusquedaSection.addSectionItem(101,"Provincia/Municipio", "gazstation");
        oBusquedaSection.addSectionItem(102, " Mapa(Ubicacion/Direcc)", "ic_menu_mapmode");
        sectionList.add(oBusquedaSection);
        
        Section oAlertSection = new Section("Alertas");
        oAlertSection.addSectionItem(150," Listado Activas", "ic_menu_reminder");
        sectionList.add(oAlertSection);
        
        Section oAjusteSection = new Section("Configuracion");
        oAjusteSection.addSectionItem(201," Ajustes", "ic_menu_preferences");
        sectionList.add(oAjusteSection);
        
        Section oOtrosSection = new Section("Otros");
        oOtrosSection.addSectionItem(301," Acerca de Gasofa", "ic_menu_contact");
        oOtrosSection.addSectionItem(302, " Salir", "ic_menu_revert");
          
        sectionList.add(oOtrosSection);
        return sectionList;
    }
   
   
    @SuppressWarnings("static-access")
	@Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {
    	ContextThemeWrapper ctw = new ContextThemeWrapper( getActivity(), R.style.miestilo);
    	  final WebView webview = (WebView)  ((Activity) getActivity()).findViewById(R.id.mainWebView);
    	  final MainActivity ma=new MainActivity();
    	  final MapActivity map= new MapActivity();
        switch ((int)id) {
       
        case 102:        	
        	getActivity().runOnUiThread(new Runnable() {
    	        @Override
    	        public void run() {
    	        	webview.loadUrl(JAVASCRIPT + "loadcomb" + BRC_OPEN
    						+ BRC_CLOSE); 
    	        	ma.slidingMenu.toggle();
    	        }
    	    });
            break;
        case 201:
        	ma.slidingMenu.toggle();
    		startActivity(new Intent(getActivity(), PrefesActivity.class));
    		//getActivity().startService(new Intent(getActivity(), UpdaterService.class));
            break;
     
        case 301:
        	
        	AlertDialog.Builder dialog = new AlertDialog.Builder(ctw);
    		dialog.setTitle("€ Gasofa V 3.1");
    		dialog.setMessage("Desarrollado" + " por J.R.  "
  					+ "email: jrmh@ya.com  ");
    		dialog.setPositiveButton("OK", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.cancel();
    			}
    		});
    		dialog.show();
    		ma.slidingMenu.toggle();
            break;
        case 302:
        	
        	AlertDialog.Builder alert = new AlertDialog.Builder(ctw);
			alert.setMessage("Salir de Gasofa");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					if(map.actividad!=null)
		        		map.actividad.finish();
		        	if(ma.actividad!=null)
		        		ma.actividad.finish();
		        	getActivity().finish();
				}
				});
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
					  ma.slidingMenu.toggle();
				  }
				});

				alert.show();	
        	
            break;
            
        case 150:
        	try{
        	ma.salir= false;
        	ma.slidingMenu.toggle();
        	bd = new BdGas(getActivity());
        	ArrayList<Alerta> alalert= bd.readBdAlert(null);
        	json= getAllRestJSONAlertas(alalert);
			
				if (json.equals(EMPTY_ALERT_LIST)){ 	
					Toast.makeText(getActivity(), "No hay alertas Activas",
					Toast.LENGTH_LONG).show();
				}else{
					Log.i("Alertas activas", json);
			getActivity().runOnUiThread(new Runnable() {
		        @Override
		        public void run() {
		        	webview.loadUrl(JAVASCRIPT + "setRestAlert" + BRC_OPEN + json + Q + flat_alerta
							+ BRC_CLOSE);
		        }
		    });
			flat_alerta=false;
			}}catch(Exception e){
				Log.i("err", e.getMessage());
			}
            break;
        }
       
        return false;
    }
/*
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		boolean alerta = arg0.getBoolean("alerta", false);
		
		if(alerta && arg1.equals("alerta") ){
			
		        	getActivity().startService(new Intent(getActivity(), UpdaterService.class));
		       
		  		
		
		}
		flat=false;
		
	}
*/
	public static String getAllRestJSONAlertas(
			ArrayList<Alerta> alalertas) {
		StringWriter writer = new StringWriter();
		ListaAlertas listaalertas = new ListaAlertas();
		listaalertas.setListaalertas(alalertas)  ;
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(writer, listaalertas);
		} catch (Exception e) {

			return EMPTY_ALERT_LIST;
		}

		return writer.toString();
	}

	
	
	
}