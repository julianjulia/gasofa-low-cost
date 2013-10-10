package com.androidmobile.model;

public class Provincia {

	String id_provincia;
	String nombre_provincia;
	public Provincia(String id_provincia, String nombre_provincia) {
		this.id_provincia = id_provincia;
		this.nombre_provincia = nombre_provincia;
	}
	/**
	 * @return the id_provincia
	 */
	public String getId_provincia() {
		return id_provincia;
	}
	/**
	 * @param id_provincia the id_provincia to set
	 */
	public void setId_provincia(String id_provincia) {
		this.id_provincia = id_provincia;
	}
	/**
	 * @return the nombre_provincia
	 */
	public String getNombre_provincia() {
		return nombre_provincia;
	}
	/**
	 * @param nombre_provincia the nombre_provincia to set
	 */
	public void setNombre_provincia(String nombre_provincia) {
		this.nombre_provincia = nombre_provincia;
	}
	
	
	
	
}
