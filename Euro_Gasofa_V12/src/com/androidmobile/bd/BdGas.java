package com.androidmobile.bd;

import java.util.ArrayList;

import com.androidmobile.model.Alerta;
import com.androidmobile.model.DatosIni;
import com.androidmobile.model.Gasolinera;
import com.androidmobile.model.Municipio;
import com.androidmobile.model.Provincia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class BdGas {

	public static GasSQLiteHelper usdbh = null;
	Context contexto;
	public BdGas(Context contexto) {
        this.contexto=contexto;
		usdbh = new GasSQLiteHelper(contexto, "BdGas", null, 6);
	}

	public void writerBdGas(ArrayList<Gasolinera> alGas) {
		SQLiteDatabase dbw = usdbh.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		try {
			if (dbw != null) {
				dbw.delete("Gas", null, null);
				for (Gasolinera g : alGas) {
					dbw.execSQL("INSERT INTO Gas (UPV,direccion, fecha,horario,gasoleo,gasolina95,gasolina98,localidad,nombre,provincia) "
							+ "VALUES ('"
							+ g.getUPV()
							+ "', '"
							+ g.getDireccion()
							+ "', '"
							+ g.getFecha()
							+ "', '"
							+ g.getHorario()
							+ "', '"
							+ g.getGasoleo()
							+ "', '"
							+ g.getGasolina95()
							+ "', '"
							+ g.getGasolina98()
							+ "', '"
							+ g.getLocalidad()
							+ "', '"
							+ g.getNombre()
							+ "', '"
							+ g.getProvincia() 
							+ "')");

				}

			}
		} finally {
			dbw.close();
		}
	}

		
	public ArrayList<Gasolinera> readBdGas(String OrderBy) {
		Gasolinera gas = null;

		ArrayList<Gasolinera> algas = new ArrayList<Gasolinera>();
		SQLiteDatabase dbr = usdbh.getReadableDatabase();
		try {
			String[] campos = new String[] {"UPV", "fecha", "nombre",
					"direccion", "localidad", "provincia", "horario","gasolina95",
					"gasolina98", "gasoleo" };
			Cursor c = dbr
					.query("Gas", campos, null, null, null, null, OrderBy);
			try {
				if (c != null) {
					if (c.moveToFirst()) {
						// Recorremos el cursor hasta que no haya más registros
						do {
							gas = new Gasolinera(c.getString(0),
									c.getString(1), c.getString(2),
									c.getString(3), c.getString(4),
									c.getString(5), c.getString(6),
									c.getString(7),c.getString(8),c.getString(9),"false");
							algas.add(gas);
						} while (c.moveToNext());
					}
				}
			} finally {
				c.close();
			}

		} finally {
			dbr.close();
		}
		return algas;

	}

	public Gasolinera obtenerGas(String valor) {
		Gasolinera gas = null;

		SQLiteDatabase dbr = usdbh.getReadableDatabase();
		try {
			String[] campos = new String[] {"UPV", "fecha", "nombre",
					"direccion", "localidad", "provincia", "horario","gasolina95",
					"gasolina98", "gasoleo" };
			
			String[] selArg = new String[] { valor };

			Cursor c = dbr.query("Gas", campos, "UPV=?", selArg,
					null, null, null);
			
										
			try {
				if (c != null) {
					if (c.moveToFirst()) {
						// Recorremos el cursor hasta que no haya más registros
						do {
							gas = new Gasolinera(c.getString(0),
									c.getString(1), c.getString(2),
									c.getString(3), c.getString(4),
									c.getString(5), c.getString(6),
									c.getString(7),c.getString(8),c.getString(9),"false");
							
						} while (c.moveToNext());
					}
				}
			} finally {
				c.close();
			}

		} finally {
			dbr.close();
		}
		return gas;

	}

	public void writerBdFavoritos(DatosIni fav) {
	
		SQLiteDatabase dbw = usdbh.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		try {
			if (dbw != null) {
				
				try {					
					dbw.execSQL("INSERT INTO Favoritos (id,icon,ref, combustible ,id_provincia ,des_provincia, municipio ,direccion, num, cp, fecha) "
							+ "VALUES ('"
							+ fav.getId()
							+ "', '"
							+ fav.getIcon()
							+ "', '"
							+ fav.getRef()
							+ "', '"
							+ fav.getCombustible()
							+ "', '"
							+ fav.getProvincia()
							+ "', '"
							+ fav.getDes_provincia()
							+ "', '"
							+ fav.getMunicipio()
							+ "', '"
							+ fav.getDireccion()
							+ "', '"
							+ fav.getNum()
							+ "', '"
							+ fav.getCp()
							+ "', '"
							+ fav.getFecha()
							+ "')");
							
					
				} catch(Exception e) {
					
				}
			}
		} finally {
			dbw.close();
		}
	}
	
	
	
	public ArrayList<DatosIni> readBdFav() {
		DatosIni fav = null;
		ArrayList<DatosIni> alfav = new ArrayList<DatosIni>();

		SQLiteDatabase dbr = usdbh.getWritableDatabase();
		try {
			String[] campos = new String[] { "id","icon","ref", "combustible" ,"id_provincia","des_provincia" ,"municipio" ,"direccion", "num", "cp","fecha" };
			// String[] args = new String[] { "%666%" };
			Cursor c = dbr.query("Favoritos", campos, null, null, null, null,
					"fecha desc");
			try {
				// Nos aseguramos de que existe al menos un registro
				if (c.moveToFirst()) {
					// Recorremos el cursor hasta que no haya más registros
					do {
						fav = new DatosIni(c.getString(0), c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getLong(10));
						alfav.add(fav);
					} while (c.moveToNext());
				}
			} finally {
				c.close();
			}

			return alfav;
		} finally {
			dbr.close();
		}
	}
	
	
	public void updateBdFavoritos(DatosIni fav) {
		SQLiteDatabase dbw = usdbh.getWritableDatabase();
		ContentValues valores= new ContentValues();
		String id=null;
		try {
			if (dbw != null) {
				Cursor c = null;
				try {
					String[] campos = new String[] { "id","icon","ref", "combustible" ,"id_provincia","des_provincia" ,"municipio" ,"direccion", "num", "cp","fecha" };
					// String[] args = new String[] { "%666%" };
					String[] selArg = new String[] { fav.getCombustible(),fav.getDireccion(),fav.getNum(),fav.getMunicipio(),fav.getCp(),fav.getDes_provincia() };

					 c= dbw.query("Favoritos", campos, "combustible=? and direccion=? and num=? and municipio=? and cp=? and des_provincia=?", selArg,
							null, null, null);
				
						// Nos aseguramos de que existe al menos un registro
						if (c.moveToFirst()) {
							id=c.getString(0);
						}else{
							id=null;
						}
					} finally {
						c.close();
					}
			}
		} finally {
			dbw.close();
		}
		
		if(id!=null){
			 dbw = usdbh.getWritableDatabase();
			try {
				if (dbw != null) {
			String[] selArgs = new String[] {id};
			valores.put("fecha", fav.getFecha());
			dbw.update("Favoritos", valores, "id=?", selArgs);
				}
				} finally {
					dbw.close();
				}	
			
		}else{	
			updateFav(fav);
				}
	}

	public void updateFav(DatosIni fav){
		ArrayList<DatosIni> aldatos=readBdFav();
		ContentValues valores= new ContentValues();
		SQLiteDatabase dbw = usdbh.getWritableDatabase();
			try {
				if (dbw != null) {
					
		String[] selArg = new String[] { aldatos.get(aldatos.size()-1).getId()  };
	// Si hemos abierto correctamente la base de datos
					String icono;
					switch (Integer.parseInt(fav.getCombustible())) {
					case 1:
						icono="gasolina95";
						break;
					case 3:
						icono="gasolina98";
						break;
					case 4:
						icono="gasoil";
						break;
					default:
						icono="gasoil";
						break;
					}
		            valores.put("icon",icono);
					//valores.put("ref", fav.getRef());
					valores.put("combustible", fav.getCombustible());
					valores.put("id_provincia",fav.getProvincia());
					valores.put("des_provincia", fav.getDes_provincia());
					valores.put("municipio",fav.getMunicipio());
					valores.put("direccion",fav.getDireccion());
					valores.put("num",fav.getNum());
					valores.put("cp",fav.getCp());
					valores.put("fecha", fav.getFecha());
					
					dbw.update("Favoritos", valores, "id=?", selArg);

				}
			} finally {
				dbw.close();
			}
	}
	
	public void writerBdProvincias(ArrayList<Provincia> alProv) {
		SQLiteDatabase dbw = usdbh.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		try {
			if (dbw != null) {
				dbw.delete("Provincias", null, null);
				for (Provincia p : alProv) {
					dbw.execSQL("INSERT INTO Provincias (id_provincia,nombre_provincia) "
							+ "VALUES ('"
							+ p.getId_provincia()
							+ "', '"
							+ p.getNombre_provincia()
							+ "')");

				}

			}
		} finally {
			dbw.close();
		}
	}

	public ArrayList<Provincia> readBdProv() {
		Provincia provincia = null;
		ArrayList<Provincia> alprov = new ArrayList<Provincia>();

		SQLiteDatabase dbr = usdbh.getWritableDatabase();
		try {
			String[] campos = new String[] { "id_provincia", "nombre_provincia" };
			// String[] args = new String[] { "%666%" };
			Cursor c = dbr.query("Provincias", campos, null, null, null, null,
					null);
			try {
				// Nos aseguramos de que existe al menos un registro
				if (c.moveToFirst()) {
					// Recorremos el cursor hasta que no haya más registros
					do {
						provincia = new Provincia(c.getString(0), c.getString(1));
						alprov.add(provincia);
					} while (c.moveToNext());
				}
			} finally {
				c.close();
			}

			return alprov;
		} finally {
			dbr.close();
		}
	}

	
	
	public void writerBdMunicipio(ArrayList<Municipio> alMun) {
		SQLiteDatabase dbw = usdbh.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		try {
			if (dbw != null) {
				//dbw.delete("Municipio", null, null);
				for (Municipio m : alMun) {
					dbw.execSQL("INSERT INTO Municipio (id_provincia,nombre_municipio) "
							+ "VALUES ('"
							+ m.getCod()
							+ "', '"
							+ m.getMunicipio()
							+ "')");

				}

			}
		} finally {
			dbw.close();
		}
	}
	
	public ArrayList<Alerta> readBdAlert(String OrderBy) {
		Alerta alert = null;

		ArrayList<Alerta> alAlert = new ArrayList<Alerta>();
		SQLiteDatabase dbr = usdbh.getReadableDatabase();
		try {
			String[] campos = new String[] {"UPV","pvp","id_comb", "nombre", "direccion","localidad", "provincia","condicion", "vibrate","sound", "runflag" };
			Cursor c = dbr
					.query("Alertas", campos, null, null, null, null, OrderBy);
			try {
				if (c != null) {
					if (c.moveToFirst()) {
						// Recorremos el cursor hasta que no haya más registros
						do {
							alert = new Alerta(c.getString(0),c.getDouble(1),c.getString(2),
									c.getString(3), c.getString(4),
									c.getString(5), c.getString(6),
									c.getString(7), c.getString(8),
									c.getString(9),c.getString(10));
							alAlert.add(alert);
						} while (c.moveToNext());
					}
				}
			} finally {
				c.close();
			}

		} finally {
			dbr.close();
		}
		return alAlert;

	}

	public void writerBdAlertas(ArrayList<Alerta> alAlert) {
		SQLiteDatabase dbw = usdbh.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		try {
			if (dbw != null) {
				//dbw.delete("Alertas", null, null);
				for (Alerta a : alAlert) {
					dbw.execSQL("INSERT INTO Alertas (UPV,pvp,id_comb, nombre, direccion,localidad, provincia,condicion, vibrate,sound, runflag) "
							+ "VALUES ('"
							+ a.getUPV()
							+ "', '"
							+ a.getPvp()
							+ "', '"
							+ a.getId_comb()
							+ "', '"
							+ a.getNombre()
							+ "', '"
							+ a.getDireccion()
							+ "', '"
							+ a.getLocalidad()
							+ "', '"
							+ a.getProvincia()
							+ "', '"
							+ a.getCondicion()
							+ "', '"
							+ a.getVibrate()
							+ "', '"
							+ a.getSound()
							+ "', '"
							+ a.getRunflag()
							+ "')");

				}

			}
		} finally {
			dbw.close();
		}
		Toast.makeText(contexto, "La alerta ha sido activada", Toast.LENGTH_SHORT).show();
	}
	public void elimiarBdAlertas(String upv,String id_com) {
		SQLiteDatabase dbw = usdbh.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		try {
			if (dbw != null) {
				String[] selArg = new String[] { upv,id_com };
				int cant=dbw.delete("Alertas", "UPV=? and id_comb=?",selArg);
				 if (cant==1)
			          Toast.makeText(contexto, "La alerta ha sido elimiada", Toast.LENGTH_SHORT).show();
			        else
			            Toast.makeText(contexto, "Error al intentar eliminar", Toast.LENGTH_SHORT).show(); 
			}
		} finally {
			dbw.close();
		}
	}
	public ArrayList<Alerta> buscarAlerta(String upv,String id_com){
		Alerta alert = null;

		ArrayList<Alerta> alAlert = new ArrayList<Alerta>();
	SQLiteDatabase dbw = usdbh.getWritableDatabase();
	try {	
		if (dbw != null) {
			Cursor c = null;
			try {
				String[] campos = new String[] {"UPV","pvp","id_comb", "nombre", "direccion","localidad", "provincia","condicion", "vibrate","sound", "runflag" };
				String[] selArg = new String[] { upv,id_com };

				 c= dbw.query("Alertas", campos, "UPV=? and id_comb=?", selArg,
						null, null, null);
			
					// Nos aseguramos de que existe al menos un registro
					if (c.moveToFirst()) {
						// Recorremos el cursor hasta que no haya más registros
						do {
							alert = new Alerta(c.getString(0),c.getDouble(1),c.getString(2),
									c.getString(3), c.getString(4),
									c.getString(5), c.getString(6),
									c.getString(7), c.getString(8),
									c.getString(9),c.getString(10));
							alAlert.add(alert);
						} while (c.moveToNext());
						return alAlert;
					}
				} finally {
					c.close();
				}
		}
	
	}
	 finally {
		dbw.close();
		}
	return null;
											}
	
	public void updateAlerta(String UPV,String id_comb,String condicion,String runflag){
		
		ContentValues valores= new ContentValues();
		SQLiteDatabase dbw = usdbh.getWritableDatabase();
			try {
				if (dbw != null) {
					
		String[] selArg = new String[] {UPV,id_comb};
	// Si hemos abierto correctamente laUPV base de datos
				    if(runflag.equals("true")){
				    	valores.put("condicion",condicion);
						valores.put("runflag", runflag);
				    }else{
				    	valores.put("condicion","");
						valores.put("runflag", runflag);
						valores.put("pvp", condicion);
				    }			    	
		            
		
					
				int t=	dbw.update("Alertas", valores, "UPV=? and id_comb=?", selArg);
				System.out.println(t);
				}
			} finally {
				dbw.close();
			}
	}

	
	
	public Cursor getStatusAlertas() {
		
		SQLiteDatabase db = usdbh.getReadableDatabase();
		//String[] campos = new String[] {"UPV","pvp","id_comb", "nombre", "direccion","localidad", "provincia","condicion", "vibrate","sound", "runflag" };
		String[] campos = new String[] {"_id","nombre","condicion", "pvp" };
		String[] selArg = new String[] { "true" };
		Cursor c= db.query("Alertas", campos, "runflag=? ", selArg,
				null, null, null);
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				int s=c.getInt(0);
						String s1=c.getString(1);
						String s2=c.getString(2);
						String s3=c.getDouble(3)+"";
						System.out.println(s3);
				
			} while (c.moveToNext());
			
		}
		return c;
	}
	
	public ArrayList<Alerta> _getStatusAlertas() {
		Alerta alert = null;
		ArrayList<Alerta> alAlert = new ArrayList<Alerta>();
		SQLiteDatabase db = usdbh.getReadableDatabase();
		try {	
			if (db != null) {
				Cursor c = null;
				try {
					String[] campos = new String[] {"UPV","pvp","id_comb", "nombre", "direccion","localidad", "provincia","condicion", "vibrate","sound", "runflag" };
					String[] selArg = new String[] { "true" };

					 c= db.query("Alertas", campos, "runflag=?", selArg,
							null, null, null);
				
						// Nos aseguramos de que existe al menos un registro
						if (c.moveToFirst()) {
							// Recorremos el cursor hasta que no haya más registros
							do {
								alert = new Alerta(c.getString(0),c.getDouble(1),c.getString(2),
										c.getString(3), c.getString(4),
										c.getString(5), c.getString(6),
										c.getString(7), c.getString(8),
										c.getString(9),c.getString(10));
								alAlert.add(alert);
							} while (c.moveToNext());
							return alAlert;
						}
					} finally {
						c.close();
					}
			}
		
		}
		 finally {
			db.close();
			}
		return null;
	}
}