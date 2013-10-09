package com.androidmobile.model;

public class Municipio {

	String cod;
	String municipio;
	public Municipio(String cod, String municipio) {
		
		this.cod = cod;
		this.municipio = municipio;
	}
	/**
	 * @return the cod
	 */
	public String getCod() {
		return cod;
	}
	/**
	 * @param cod the cod to set
	 */
	public void setCod(String cod) {
		this.cod = cod;
	}
	/**
	 * @return the municipio
	 */
	public String getMunicipio() {
		return municipio;
	}
	/**
	 * @param municipio the municipio to set
	 */
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	
	
	
}
