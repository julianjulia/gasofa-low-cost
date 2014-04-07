package com.androidmobile.menulateral;

import java.util.ArrayList;

import java.util.List;

import com.androidmobile.JR.MainActivity;
import com.androidmobile.JR.MapActivity;
import com.androidmobile.JR.R;
import com.androidmobile.JR.R.id;
import com.androidmobile.JR.R.layout;
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



public class SlidingMenuFragmentMap extends Fragment implements ExpandableListView.OnChildClickListener {
	   
    private ExpandableListView sectionListView;
   
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
     
             
        Section oOtrosSection = new Section("Otros");
        oOtrosSection.addSectionItem(301," Acerca de Gasofa", "ic_menu_contact");
        oOtrosSection.addSectionItem(302, " Salir", "ic_menu_revert");
        
        sectionList.add(oBusquedaSection);
        sectionList.add(oGeneralSection);
        sectionList.add(oOtrosSection);
        return sectionList;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {
 SupportMapFragment fm = (SupportMapFragment) ((FragmentActivity) getActivity()).getSupportFragmentManager().findFragmentById(R.id.map);
 		ContextThemeWrapper ctw = new ContextThemeWrapper( getActivity(), R.style.miestilo);		 
 		final MapActivity map= new MapActivity();
		 final MainActivity ma=new MainActivity();
         // Getting GoogleMap object from the fragment
        GoogleMap mapa = fm.getMap();	
        switch ((int)id) {
        case 101:
        	if (ma.actividad!=null)
        		ma.actividad.finish();
        	getActivity().finish();
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
        	map.slidingMenu.toggle();
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
        		map.slidingMenu.toggle();
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
					  map.slidingMenu.toggle();
				  }
				});

				alert.show();	
            break;
        }
       
        return false;
    }
}
