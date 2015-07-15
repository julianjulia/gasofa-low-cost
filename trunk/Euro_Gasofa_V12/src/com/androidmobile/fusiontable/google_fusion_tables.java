package com.androidmobile.fusiontable;


class google_fusion_tables {

FusionTable ft;
 
void setup() {
  String tableId = "1Em2OUwEI0wrZCFU0FJO8wo8tA1Q_TFhlZY_AVvw";   
  ft = new FusionTable(tableId);   
}
 
void draw() {
}
 
void mouseReleased() {
  // Obtengo un listado del campo "texto" de todas las entradas:
  String respuesta = ft.selectAll("texto");
  String[] lineas = respuesta.split("\n");
  //println(lineas);
  
  // Obtengo un listado del campo "id" para aquellas entrada 
  // cuyo campo de nombre "texto" tenga el valor "prueba".
  respuesta = ft.select("id","texto='prueba'");
  lineas = respuesta.split("\n");
  //println(lineas);
  
  // Inserto una nueva fila en la tabla que da valores
  // a los campos "texto" e "id"
  respuesta = ft.insert("(texto,id)", "('prueba',100)");
  //println(respuesta);
  
  // Actualizo el campo "id" de la primera entrada de la tabla
  // que tenga como texto el valor "prueba"
  respuesta = ft.update("texto='prueba'", "id=101");
  //println(respuesta);
}
}
