package com.androidmobile.bd;

import java.util.ArrayList;

import com.androidmobile.model.Favoritos;
import com.androidmobile.model.Gasolinera;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BdGas {

	public GasSQLiteHelper usdbh = null;

	public BdGas(Context contexto) {

		usdbh = new GasSQLiteHelper(contexto, "BdGas", null, 1);
	}

	public void writerBdGas(ArrayList<Gasolinera> alGas) {
		SQLiteDatabase dbw = usdbh.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		try {
			if (dbw != null) {
				dbw.delete("Gas", null, null);
				for (Gasolinera g : alGas) {
					dbw.execSQL("INSERT INTO Gas (direccion, distancia,gasoleo,gasolina95,gasolina98,localidad,nombre,provincia) "
							+ "VALUES ('"
							+ g.getDireccion()
							+ "', '"
							+ g.getDistancia()
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
							+ g.getProvincia() + "')");

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
			String[] campos = new String[] { "distancia", "nombre",
					"direccion", "localidad", "provincia", "gasolina95",
					"gasolina98", "gasoleo" };
			Cursor c = dbr
					.query("Gas", campos, null, null, null, null, OrderBy);
			try {
				if (c != null) {
					if (c.moveToFirst()) {
						// Recorremos el cursor hasta que no haya más registros
						do {
							gas = new Gasolinera(c.getDouble(0),
									c.getString(1), c.getString(2),
									c.getString(3), c.getString(4),
									c.getString(5), c.getString(6),
									c.getString(7));
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

}
