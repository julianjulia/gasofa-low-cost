package com.androidmobile.bd;

import java.util.ArrayList;

import com.androidmobile.model.Favoritos;
import com.androidmobile.model.Gasolinera;
import com.androidmobile.model.Municipio;
import com.androidmobile.model.Provincia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BdGas {

	public static GasSQLiteHelper usdbh = null;

	public BdGas(Context contexto) {

		usdbh = new GasSQLiteHelper(contexto, "BdGas", null, 2);
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

		
	public void writerBdFavoritos(Favoritos fav) {
		SQLiteDatabase dbw = usdbh.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		try {
			if (dbw != null) {
				String[] campos = new String[] { "pos", "nombre" };
				String[] selArg = new String[] { fav.getPos() + "" };

				Cursor c = dbw.query("Favoritos", campos, "pos=?", selArg,
						null, null, null);
				try {
					if (c.getCount() == 0) {
						dbw.execSQL("INSERT INTO Favoritos (pos, nombre) "
								+ "VALUES (" + fav.getPos() + ", '"
								+ fav.getNombre() + "')");
					} else {
						// Establecemos los campos-valores a actualizar
						ContentValues valores = new ContentValues();
						valores.put("nombre", fav.getNombre());
						String[] selArgs = new String[] { fav.getPos() + "" };
						dbw.update("Favoritos", valores, "pos=?", selArgs);

					}
				} finally {
					c.close();
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
									c.getString(7),c.getString(8),c.getString(9));
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
									c.getString(7),c.getString(8),c.getString(9));
							
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

	
	
	public ArrayList<Favoritos> readBdFav() {
		Favoritos fav = null;
		ArrayList<Favoritos> alfav = new ArrayList<Favoritos>();

		SQLiteDatabase dbr = usdbh.getWritableDatabase();
		try {
			String[] campos = new String[] { "pos", "nombre" };
			// String[] args = new String[] { "%666%" };
			Cursor c = dbr.query("Favoritos", campos, null, null, null, null,
					null);
			try {
				// Nos aseguramos de que existe al menos un registro
				if (c.moveToFirst()) {
					// Recorremos el cursor hasta que no haya más registros
					do {
						fav = new Favoritos(c.getInt(0), c.getString(1));
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
	
	
	
	public ArrayList<Municipio> obtenerMunicipios(String valor) {
			Municipio mun = null;
			ArrayList<Municipio> almun = new ArrayList<Municipio>();

			SQLiteDatabase dbr = usdbh.getWritableDatabase();
			try {
				String[] campos = new String[] { "id_provincia", "nombre_municipio" };
				 String[] args = new String[] { valor };
				
				Cursor c = dbr.query("Municipio", campos, "id_provincia=?", args,
						null, null, null);
				try {
					if (c != null && c.getCount()!=0) {
					// Nos aseguramos de que existe al menos un registro
					if (c.moveToFirst()) {
						// Recorremos el cursor hasta que no haya más registros
						do {
							mun = new Municipio(c.getString(0), c.getString(1));
							almun.add(mun);
						} while (c.moveToNext());
					}
					}else{
					return null;	
					}
				} finally {
					c.close();
				}

				return almun;
			} finally {
				dbr.close();
			}
		}
}
