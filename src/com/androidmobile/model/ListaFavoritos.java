package com.androidmobile.model;

import java.util.ArrayList;

public class ListaFavoritos {

	ArrayList<Favoritos> listaFavoritos;

	public ListaFavoritos() {

		listaFavoritos = new ArrayList<Favoritos>();

	}

	public ArrayList<Favoritos> getListaFavoritos() {
		return listaFavoritos;
	}

	public void setListaFavoritos(ArrayList<Favoritos> listaFavoritos) {
		this.listaFavoritos = listaFavoritos;
	}

}
