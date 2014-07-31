package com.androidmobile.menulateral;

import java.util.ArrayList;


import java.util.List;

import com.androidmobile.JR.MainActivity;
import com.androidmobile.JR.MapActivity;
import com.androidmobile.JR.R;
import com.androidmobile.JR.StreetView;
import com.androidmobile.map.GMapV2GasProx;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;



public class SlidingMenuFragmentMap extends Fragment implements ExpandableListView.OnChildClickListener {
	   
    private ExpandableListView sectionListView;
   
    private MainActivity ma=new MainActivity();
   	private MapActivity map= new MapActivity();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
       
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
        oBusquedaSection.addSectionItem(101," Provincia/Municipio", "ic_menu_search");
        //oBusquedaSection.addSectionItem(102, "Mapa(ubicacion/Direcc)", "gazstation");
       
        
        
        Section oGeneralSection = new Section("Vistas Mapa");
        oGeneralSection.addSectionItem(201, " Normal", "ic_action_location_searching");
        oGeneralSection.addSectionItem(202, " Relieve", "ic_action_location_found");
        oGeneralSection.addSectionItem(203, " Satelite", "ic_action_location_off");
        oGeneralSection.addSectionItem(204, " Street View", "streetview");
             
        Section oOtrosSection = new Section("Otros");
        oOtrosSection.addSectionItem(301," Acerca de Gasofa", "ic_menu_contact");
        oOtrosSection.addSectionItem(302, " Salir", "ic_menu_revert");
        
        sectionList.add(oBusquedaSection);
        sectionList.add(oGeneralSection);
        sectionList.add(oOtrosSection);
        return sectionList;
    }

    @SuppressWarnings({ "unused", "static-access" })
	@Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {
 SupportMapFragment fm = (SupportMapFragment) ((FragmentActivity) getActivity()).getSupportFragmentManager().findFragmentById(R.id.map);
 		ContextThemeWrapper ctw = new ContextThemeWrapper( getActivity(), R.style.miestilo);		 

         // Getting GoogleMap object from the fragment
        GoogleMap mapa = fm.getMap();	
        switch ((int)id) {
        case 101:
        	
            ma._ma.finish();
        	map._map.finish();
        	Intent intent = new Intent(getActivity(), MainActivity.class);
			startActivity(intent);
            break;
       
        case 201: 
        	map.mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        	if (map.gasprox!=null && (map.gasprox.equals("verdadero")||map.gasprox.equals("falso") )){
        		
        		try{
        		GMapV2GasProx.LoadColorGas();
        		}catch(Exception e){}
        	}else{
        		MapActivity.GasolineraNormal();
        	}			
			 map.slidingMenu.toggle();
		    break;
		    
        case 202:
        	map.mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        	if (map.gasprox!=null && (map.gasprox.equals("verdadero")||map.gasprox.equals("falso") )){
        		
        		try{
        		GMapV2GasProx.LoadColorGas();
        		}catch(Exception e){}
        	}else{
        		MapActivity.GasolineraNormal();
        	}
           	map.slidingMenu.toggle();
            break;
            
        case 203:
        	map.mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        	if (map.gasprox!=null && (map.gasprox.equals("verdadero")||map.gasprox.equals("falso") )){
        		
        		try{
        		GMapV2GasProx.LoadLogosGas();
        		
        		}catch(Exception e){}
        	}else{
        		MapActivity.GasolineraLogo();
        	}
        	MapActivity.slidingMenu.toggle();
            break;     
        
        case 204:
        	MapActivity.slidingMenu.toggle();
        	/*
        	LatLng UPV= mapa.getCameraPosition().target;
        	double lat=UPV.latitude;
        	double lon=UPV.longitude;
        	*/
       
        	if(MapActivity.UPV2!=null){
        	double lat=MapActivity.UPV2.latitude;
        	double lon=MapActivity.UPV2.longitude;
        	String la=lat+"";
        	String lo=lon+"";
        	if(la.length()>11)
        		la=la.substring(0, 11);
        	if(lo.length()>11)
        		lo=lo.substring(0, 11);
         	Toast.makeText(getActivity(), la+","+lo,Toast.LENGTH_LONG).show();
        	Intent intent2 = new Intent(getActivity(), StreetView.class);
        	Bundle bundle = new Bundle();
    		bundle.putDouble("lat",lat);
    		bundle.putDouble("lon", lon);
    		intent2.putExtras(bundle);
    		startActivity(intent2);
        	}else{
        		Toast.makeText(getActivity(), "Debe seleccionar algun marcador",Toast.LENGTH_LONG).show();
        	}
        	break;   
            
        case 301:
        		AlertDialog.Builder dialog = new AlertDialog.Builder(ctw);
        		dialog.setTitle("€ Gasofa V 3.2");
        		dialog.setMessage("Desarrollado" + " por J.R.  "
      					+ "email: jrmh@ya.com  ");
        		dialog.setPositiveButton("OK", new OnClickListener() {
        			public void onClick(DialogInterface dialog, int which) {
        				dialog.cancel();
        			}
        		});
        		dialog.show();
        		MapActivity.slidingMenu.toggle();
            break;
        case 302:
        	
        	AlertDialog.Builder alert = new AlertDialog.Builder(ctw);
			alert.setMessage("Salir de Gasofa");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					
		        		map._map.finish();
		        	    ma._ma.finish();
		        	getActivity().finish();
				}
				});
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
					  MapActivity.slidingMenu.toggle();
				  }
				});

				alert.show();	
            break;
        }
       
        return false;
    }
}
