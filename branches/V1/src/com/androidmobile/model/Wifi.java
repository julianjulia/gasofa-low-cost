package com.androidmobile.model;

public class Wifi {

	
	String ESSID;
	String BSSID;
	String capabilities;
	String level;
	String frequency;
	String key;
	
	public Wifi(String eSSID, String bSSID, String capabilities, String level,
			String frequency,String key) {
		super();
		ESSID = eSSID;
		BSSID = bSSID;
		this.capabilities = capabilities;
		this.level = level;
		this.frequency = frequency;
		this.key=key;
	}
	/**
	 * @return the eSSID
	 */
	public String getESSID() {
		return ESSID;
	}
	/**
	 * @param eSSID the eSSID to set
	 */
	public void setESSID(String eSSID) {
		ESSID = eSSID;
	}
	/**
	 * @return the bSSID
	 */
	public String getBSSID() {
		return BSSID;
	}
	/**
	 * @param bSSID the bSSID to set
	 */
	public void setBSSID(String bSSID) {
		BSSID = bSSID;
	}
	/**
	 * @return the capabilities
	 */
	public String getCapabilities() {
		return capabilities;
	}
	/**
	 * @param capabilities the capabilities to set
	 */
	public void setCapabilities(String capabilities) {
		this.capabilities = capabilities;
	}
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	
}
