package com.androidmobile.model;

public class Gasolinera {

	String fecha = "";
	String nombre = "";
	String direccion = "";
	String localidad = "";
	String provincia = "";
	String gasolina95 = "";
	String gasolina98 = "";
	String gasoleo = "";
	String horario = "";
	String UPV="";

	public Gasolinera(String UPV,String fecha, String nombre, String direccion,
			String localidad, String provincia,String horario, String gasolina95,
			String gasolina98, String gasoleo) {
		this.fecha = fecha;
		this.nombre = nombre;
		this.direccion = direccion;
		this.localidad = localidad;
		this.provincia = provincia;
		this.gasolina95 = gasolina95;
		this.gasolina98 = gasolina98;
		this.gasoleo = gasoleo;
		this.horario = horario;
		this.UPV= UPV;
	}

		
	
	/**
	 * @return the uPV
	 */
	public String getUPV() {
		return UPV;
	}



	/**
	 * @param uPV the uPV to set
	 */
	public void setUPV(String uPV) {
		UPV = uPV;
	}



	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the horario
	 */
	public String getHorario() {
		return horario;
	}

	/**
	 * @param horario the horario to set
	 */
	public void setHorario(String horario) {
		this.horario = horario;
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
