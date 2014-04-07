package com.androidmobile.model;

public class DatosIni {
	String id;
	String ref;
	String combustible;
	String provincia;
	String des_provincia;
	String municipio;
	String direccion;
	String num;
	String cp;
	String icon;
	long fecha;
	public DatosIni(String combustible, String provincia,String des_provincia, String municipio,
			String direccion, String num, String cp,long fecha) {
		this.combustible = combustible;
		this.provincia = provincia;
		this.des_provincia=des_provincia;
		this.municipio = municipio;
		this.direccion = direccion;
		this.num = num;
		this.cp = cp;
		this.fecha=fecha;
	}
	public DatosIni(String id,String icon,String ref, String combustible, String provincia,String des_provincia, String municipio,
			String direccion, String num, String cp,long fecha) {
		this.icon=icon;
		this.id=id;
		this.ref=ref;
		this.combustible = combustible;
		this.des_provincia=des_provincia;
		this.provincia = provincia;
		this.municipio = municipio;
		this.direccion = direccion;
		this.num = num;
		this.cp = cp;
		this.fecha=fecha;
	}
	
	public DatosIni() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	/**
	 * @return the des_provincia
	 */
	public String getDes_provincia() {
		return des_provincia;
	}
	/**
	 * @param des_provincia the des_provincia to set
	 */
	public void setDes_provincia(String des_provincia) {
		this.des_provincia = des_provincia;
	}
	/**
	 * @return the fecha
	 */
	public long getFecha() {
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(long fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
