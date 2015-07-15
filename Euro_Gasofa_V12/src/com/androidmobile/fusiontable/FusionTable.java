package com.androidmobile.fusiontable;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

class FusionTable {

  String baseUrl, tableId;
  String auth;
  boolean querying;

  FusionTable(String tableId_) {  
    auth = ClientLogin.authorize("julianjulia@gmail.com", "4610550");  
    tableId = tableId_; 
    baseUrl = "https://www.google.com/fusiontables/api/query";
  }

  String select(String fromField, String condition) {
    querying = true;
    String query = "SELECT " + fromField + " FROM " + tableId + " WHERE " + condition;
    //println(query);
    query = "?sql=" + URLEncoder.encode(query);
    String respuesta = RequestHandler.sendHttpRequest(baseUrl+query, "GET", null, null);
    querying = false;
    return respuesta;
  }

  String selectAll(String fromField) {
    querying = true;    
    String query = "SELECT " + fromField + " FROM " + tableId;
    //println(query);
    query = "?sql=" + URLEncoder.encode(query);
    String respuesta = RequestHandler.sendHttpRequest(baseUrl+query, "GET", null, null);
    querying = false;    
    return respuesta;
  }  

  String selectGeoCircle(String fromField, String locationField, float lat, float lon, float radio) {
    querying = true;
    String query = "SELECT " + fromField + " FROM " + tableId + " WHERE ST_INTERSECTS (" + locationField + ", CIRCLE(LATLNG(" + lat + "," + lon +"),"+ radio +"))";
    //println(query);
    query = "?sql=" + URLEncoder.encode(query);
    String respuesta = RequestHandler.sendHttpRequest(baseUrl+query, "GET", null, null);
    querying = false;    
    return respuesta;
  }

  String selectCustom(String query_) {
    querying = true;
    String query = "";
    query = "?sql=" + URLEncoder.encode(query);
    String respuesta = RequestHandler.sendHttpRequest(baseUrl+query, "GET", null, null);
    querying = false;    
    return respuesta;
  }

  String insert(String fields, String values) {
    querying = true;
    String query = "INSERT INTO " + tableId + " " + fields + " VALUES " + values;
   // println(query);
    query = "sql=" + URLEncoder.encode(query);
    Map headers = new HashMap();
    headers.put("Authorization", "GoogleLogin auth="+auth);

    String respuesta = RequestHandler.sendHttpRequest(baseUrl, "POST", query, headers);
    querying = false;    
    return respuesta; // ROWID si todo va bien
  }


  // Sólo hace update del primer registro que encuentra
  String update(String busqueda, String cambio) {
    querying = true;    
    String query = "SELECT ROWID FROM " + tableId + " WHERE " + busqueda;
    //println(query);
    query = "?sql=" + URLEncoder.encode(query);
    String respuesta = RequestHandler.sendHttpRequest(baseUrl+query, "GET", null, null);    
    if (respuesta != null) {
      String[] lineas = respuesta.split("\n");
      if (lineas.length > 1) {
        String rowid = lineas[1];
        query = "UPDATE " + tableId + " SET " + cambio + " WHERE ROWID = '" + rowid + "'";
        query = "sql=" + URLEncoder.encode(query);
        Map headers = new HashMap();
        headers.put("Authorization", "GoogleLogin auth="+auth);      
        respuesta = RequestHandler.sendHttpRequest(baseUrl, "POST", query, headers);
        querying = false;      
        return respuesta; // OK si todo va bien
      }
      else {
        querying = false;      
        return null;
      }
    }
    else {
      querying = false;      
      return null;
    }
  }

  String delete(String busqueda) {
    querying = true;    
    String query = "SELECT ROWID FROM " + tableId + " WHERE " + busqueda;
    //println(query);
    query = "?sql=" + URLEncoder.encode(query);
    String respuesta = RequestHandler.sendHttpRequest(baseUrl+query, "GET", null, null);    
    if (respuesta != null) {
      String[] lineas = respuesta.split("\n");
      int cuenta = 0;
      if (lineas.length > 1) {
        for (int n = 1; n < lineas.length; n++) {
          String rowid = lineas[n];
          query = "DELETE FROM " + tableId + " { WHERE ROWID = '" + rowid + "'}";
          query = "sql=" + URLEncoder.encode(query);
          Map headers = new HashMap();
          headers.put("Authorization", "GoogleLogin auth="+auth);      
          respuesta = RequestHandler.sendHttpRequest(baseUrl, "POST", query, headers);
          //println(respuesta);
          cuenta++;
        }
        querying = false;      
        return cuenta + " filas borradas";
      }
      else {
        querying = false;      
        return null;
      }
    }
    else {
      querying = false;      
      return null;
    }
  }
}

