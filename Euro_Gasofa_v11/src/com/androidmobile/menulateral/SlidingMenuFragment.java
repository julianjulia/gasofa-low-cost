package com.androidmobile.menulateral;

import java.util.ArrayList;


import java.util.List;

import com.androidmobile.JR.MainActivity;
import com.androidmobile.JR.MapActivity;
import com.androidmobile.JR.R;
import com.androidmobile.JR.R.id;
import com.androidmobile.JR.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ExpandableListView;


public class SlidingMenuFragment extends Fragment implements ExpandableListView.OnChildClickListener {

	private static final String JAVASCRIPT = "javascript:";
	private static final String BRC_OPEN = "('";
	private static final String BRC_CLOSE = "')";
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
        //oBusquedaSection.addSectionItem(101,"Provincia/Municipio", "gazstation");
        oBusquedaSection.addSectionItem(102, " Mapa(Ubicacion/Direcc)", "ic_action_map");
       
        /*
        
        Section oGeneralSection = new Section("Favoritos");
        int i=200;
        for (DatosIni fav :datos<arraylist>){
        i++
        oGeneralSection.addSectionItem(i, fav.getRef(), "favorito");
        } 
       
       */
        Section oOtrosSection = new Section("Otros");
        oOtrosSection.addSectionItem(301," Acerca de Gasofa", "ic_action_person");
        oOtrosSection.addSectionItem(302, " Salir", "ic_action_undo");
        
        sectionList.add(oBusquedaSection);
        //sectionList.add(oGeneralSection);
        sectionList.add(oOtrosSection);
        return sectionList;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {
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
      
        case 301:
        	AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
    		dialog.setTitle("€ Gasofa V 2.6");
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
        	ContextThemeWrapper ctw = new ContextThemeWrapper( getActivity(), R.style.miestilo);
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
        }
       
        return false;
    }
}
