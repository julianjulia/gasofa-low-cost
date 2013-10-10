package com.androidmobile.model;

public class Favoritos {

	int pos;
	String nombre;

	public Favoritos(int pos, String nombre) {
		this.pos = pos;
		this.nombre = nombre;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
