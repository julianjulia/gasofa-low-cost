package com.androidmobile.menulateral;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.androidmobile.JR.MainActivity;
import com.androidmobile.JR.R;
import com.androidmobile.bd.BdGas;
import com.androidmobile.model.DatosIni;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ExpandableListView;



public class SlidingMenuFragmentFav extends Fragment implements ExpandableListView.OnChildClickListener{

	private static final String JAVASCRIPT = "javascript:";
	private static final String BRC_OPEN = "('";
	private static final String BRC_="'";
	private static final String BRC_CLOSE = "')";
    private ExpandableListView sectionListView;
    private BdGas bd;
    ArrayList<DatosIni> datos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        bd= new BdGas(getActivity().getBaseContext());
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

        Section oBusquedaSection = new Section("  Historial");
      
        datos=bd.readBdFav();           
       
        if (datos!=null && datos.size()>0){
        for (DatosIni fav :datos){
       
        	 oBusquedaSection.addSectionItem(Long.parseLong(fav.getId()),fav.getDireccion()+" "+fav.getNum()+" "+ fav.getMunicipio()+" "+fav.getCp()+" ("+fav.getDes_provincia()+")",fav.getIcon());
        } 
        }else{
        	bd= new BdGas(getActivity().getBaseContext());
        	long fecha=new Date().getTime();
        	for(int id=201;id<209;id++){
        		bd.writerBdFavoritos(new DatosIni(String.valueOf(id),"ic_launcher2", "-----","", "", "", "", "", "", "",fecha));     
    									}
        	
        	 datos=bd.readBdFav();           
             
             if (datos!=null && datos.size()>0){
             for (DatosIni fav :datos){
                         oBusquedaSection.addSectionItem(Long.parseLong(fav.getId()), fav.getMunicipio()+" ("+fav.getDes_provincia()+")",fav.getIcon());
             						  } 
             									}
        	}
        sectionList.add(oBusquedaSection);
            
        return sectionList;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
          int groupPosition, int childPosition, long id) {
    	  final WebView webview = (WebView)  ((Activity) getActivity()).findViewById(R.id.mainWebView);
    	  final MainActivity ma=new MainActivity();
       	  ma.slidingFavoritos.toggle();
       	  DatosIni favorito= null;
       	  int i=0;
       	  for(DatosIni d:datos){
       		 if(d.getId().equals(String.valueOf(id)))
       			  favorito=datos.get(i);
       		 i++;
       	  }
       	ma.comb=favorito.getCombustible();
       	ma.direccion=favorito.getDireccion();
		ma.num=favorito.getNum();
		ma.cp=favorito.getCp();
		ma.cod_prov = favorito.getProvincia();
		ma.des_prov=favorito.getDes_provincia();
		ma.des_mun = favorito.getMunicipio();
		int valor=4;
		if(!favorito.getCombustible().equals("")){
			 valor= Integer.valueOf(favorito.getCombustible());
		}
		switch (valor) {
		case 1:
			ma.comb="gasolina95";
			break;
		case 3:
			ma.comb="gasolina98";
			break;
		case 4:
			ma.comb="gasoleo";
			break;
		default:
			ma.comb="gasoleo";
			break;
		}
    	  ma.runOnUiThread(new Runnable() {
  	        @Override
  	        public void run() {
  	        	webview.loadUrl(JAVASCRIPT + "loadFavoritos" + BRC_OPEN +ma.comb+"','"+ma.des_prov +"','"+ma.des_mun+"','"+ma.direccion+"','"+ma.num+"','"+ma.cp
						+ BRC_CLOSE); 
  	        }
  	    });
    	  ma.runOnUiThread(new Runnable() {
    	        @Override
    	        public void run() {
    	        	webview.loadUrl(JAVASCRIPT + "loadFavoritos" + BRC_OPEN +ma.comb+"','"+ma.des_prov +"','"+ma.des_mun+"','"+ma.direccion+"','"+ma.num+"','"+ma.cp
  						+ BRC_CLOSE); 
    	        }
    	    });
    	 // ma.slidingFavoritos.setMenu(R.layout.slidingfavoritos);
       
        return false;
    }

   

    
	
}
