package com.androidmobile.fusiontable;

import java.net.URLEncoder;

/**
 * Helper class for getting auth token.
 * 
 * @author kbrisbin@google.com (Kathryn Hurley)
 */
public class ClientLogin {
	  private final static String authURI =
	      "https://www.googleapis.com/fusiontables/v2/query?_" +
	      "sql=SELECT+ROWID%2C+telefono%2C+usuario+FROM+16_ZqhEkU7u0-RUDuOZgh_T8IS_U0yGSAUgSUlnH-&" +
	      "key=AIzaSyAm9yWCV7JPCTHCJut8whOjARd7pwROFDQ";

	  /**
	   * Returns the auth token if the user is successfully authorized.
	   *
	   * @param username  the username
	   * @param password  the password
	   * @return the client login token, or null if not authorized
	   */
	  public static String authorize(String username, String password) {
		return password;
	    
	  }
	}


