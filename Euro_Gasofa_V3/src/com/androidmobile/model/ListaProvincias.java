package com.androidmobile.model;

import java.util.ArrayList;

public class ListaProvincias {

	ArrayList<Provincia> listaprovincias;
	
	public ListaProvincias(){
		
		listaprovincias = new ArrayList<Provincia>();
	}

	/**
	 * @return the listaprovincias
	 */
	public ArrayList<Provincia> getListaprovincias() {
		return listaprovincias;
	}

	/**
	 * @param listaprovincias the listaprovincias to set
	 */
	public void setListaprovincias(ArrayList<Provincia> listaprovincias) {
		this.listaprovincias = listaprovincias;
	}
	
	
}
