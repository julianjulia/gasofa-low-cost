package com.androidmobile.model;

public class Gasolinera {

	Double distancia = 0.00;
	String nombre = "";
	String direccion = "";
	String localidad = "";
	String provincia = "";
	String gasolina95 = "";
	String gasolina98 = "";
	String gasoleo = "";

	public Gasolinera(Double distancia, String nombre, String direccion,
			String localidad, String provincia, String gasolina95,
			String gasolina98, String gasoleo) {
		this.distancia = distancia;
		this.nombre = nombre;
		this.direccion = direccion;
		this.localidad = localidad;
		this.provincia = provincia;
		this.gasolina95 = gasolina95;
		this.gasolina98 = gasolina98;
		this.gasoleo = gasoleo;
	}

	public Double getDistancia() {
		return distancia;
	}

	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getGasolina95() {
		return gasolina95;
	}

	public void setGasolina95(String gasolina95) {
		this.gasolina95 = gasolina95;
	}

	public String getGasolina98() {
		return gasolina98;
	}

	public void setGasolina98(String gasolina98) {
		this.gasolina98 = gasolina98;
	}

	public String getGasoleo() {
		return gasoleo;
	}

	public void setGasoleo(String gasoleo) {
		this.gasoleo = gasoleo;
	}

}
