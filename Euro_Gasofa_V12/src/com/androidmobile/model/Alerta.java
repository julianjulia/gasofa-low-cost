package com.androidmobile.model;

public class Alerta {
	String UPV;
	double pvp;
	String id_comb;
	String nombre;
	String direccion;
	String localidad;
	String provincia;
	String condicion;	//usamos para precio actual del combustible
	String vibrate;
	String sound;
	String runflag;
	public Alerta(String uPV, double pvp,String id_comb, String nombre, String direccion,
			String localidad, String provincia, String condicion,
			String vibrate, String sound, String runflag) {
		
		UPV = uPV;
		this.pvp=pvp;
		this.nombre = nombre;
		this.id_comb=id_comb;
		this.direccion = direccion;
		this.localidad = localidad;
		this.provincia = provincia;
		this.condicion = condicion;
		this.vibrate = vibrate;
		this.sound = sound;
		this.runflag = runflag;
	}
	/**Alertas
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
	 * @return the nombre
	 */
	
	
	public String getNombre() {
		return nombre;
	}
	/**
	 * @return the pvp
	 */
	public double getPvp() {
		return pvp;
	}
	
	
	/**
	 * @return the id_comb
	 */
	public String getId_comb() {
		return id_comb;
	}
	/**
	 * @param id_comb the id_comb to set
	 */
	public void setId_comb(String id_comb) {
		this.id_comb = id_comb;
	}
	/**
	 * @param pvp the pvp to set
	 */
	public void setPvp(double pvp) {
		this.pvp = pvp;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	 * @return the localidad
	 */
	public String getLocalidad() {
		return localidad;
	}
	/**
	 * @param localidad the localidad to set
	 */
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
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
	 * @return the condicion
	 */
	public String getCondicion() {
		return condicion;
	}
	/**
	 * @param condicion the condicion to set
	 */
	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}
	/**
	 * @return the vibrate
	 */
	public String getVibrate() {
		return vibrate;
	}
	/**
	 * @param vibrate the vibrate to set
	 */
	public void setVibrate(String vibrate) {
		this.vibrate = vibrate;
	}
	/**
	 * @return the sound
	 */
	public String getSound() {
		return sound;
	}
	/**
	 * @param sound the sound to set
	 */
	public void setSound(String sound) {
		this.sound = sound;
	}
	/**
	 * @return the runflag
	 */
	public String getRunflag() {
		return runflag;
	}
	/**
	 * @param runflag the runflag to set
	 */
	public void setRunflag(String runflag) {
		this.runflag = runflag;
	}

	
}
