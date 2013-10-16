package com.androidmobile.model;

public class DatosIni {
	String ref;
	String combustible;
	String provincia;
	String municipio;
	String direccion;
	String num;
	String cp;
	public DatosIni(String combustible, String provincia, String municipio,
			String direccion, String num, String cp) {
		this.combustible = combustible;
		this.provincia = provincia;
		this.municipio = municipio;
		this.direccion = direccion;
		this.num = num;
		this.cp = cp;
	}
	public DatosIni(String ref, String combustible, String provincia, String municipio,
			String direccion, String num, String cp) {
		this.ref=ref;
		this.combustible = combustible;
		this.provincia = provincia;
		this.municipio = municipio;
		this.direccion = direccion;
		this.num = num;
		this.cp = cp;
	}
	
	public DatosIni() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}
	/**
	 * @param ref the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}
	/**
	 * @return the combustible
	 */
	public String getCombustible() {
		return combustible;
	}
	/**
	 * @param combustible the combustible to set
	 */
	public void setCombustible(String combustible) {
		this.combustible = combustible;
	}
	/**
	 * @return the provincia
	 */
	public String getProvincia() {
		return provincia;
	}
	/**
	 * @param provincia the provincia to set
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
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
	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}
	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	/**
	 * @return the num
	 */
	public String getNum() {
		return num;
	}
	/**
	 * @param num the num to set
	 */
	public void setNum(String num) {
		this.num = num;
	}
	/**
	 * @return the cp
	 */
	public String getCp() {
		return cp;
	}
	/**
	 * @param cp the cp to set
	 */
	public void setCp(String cp) {
		this.cp = cp;
	}
	
	
}
