package com.androidmobile.model;

import java.util.ArrayList;

public class ListaGasolineras {

	ArrayList<Gasolinera> listagasolineras;

	public ListaGasolineras() {
		super();
		listagasolineras = new ArrayList<Gasolinera>();
	}

	public ArrayList<Gasolinera> getListagasolineras() {
		return listagasolineras;
	}

	public void setListagasolineras(ArrayList<Gasolinera> listagasolineras) {
		this.listagasolineras = listagasolineras;
	}

}
