package com.androidmobile.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GasSQLiteHelper extends SQLiteOpenHelper {

	// Sentencia SQL para crear la tabla de Usuarios
	String sqlCreate = "CREATE TABLE Favoritos (pos INTEGER, nombre TEXT)";
	String sqlCreateGas = "Create table Gas ( UPV TEXT,fecha TEXT, nombre TEXT,horario TEXT,direccion TEXT, localidad TEXT,provincia TEXT,gasolina95 TEXT,gasolina98 TEXT,gasoleo TEXT)";

	String sqlCreateProv = "CREATE TABLE Provincias (id_provincia TEXT, nombre_provincia TEXT)";
	String sqlCreateMun = "CREATE TABLE Municipio (id_provincia TEXT, nombre_municipio TEXT)";
	String sqlCreateInc = "CREATE TABLE Ini(combustible TEXT,provincia TEXT,municipio TEXT,direccion TEXT, num TEXT, cp TEXT)";
	public GasSQLiteHelper(Context contexto, String nombre,
			CursorFactory factory, int version) {
	
		super(contexto, nombre, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Se ejecuta la sentencia SQL de creación de la tabla
		db.execSQL(sqlCreate);
		db.execSQL(sqlCreateGas);
		
		db.execSQL(sqlCreateProv);		
		db.execSQL(sqlCreateMun);	
		db.execSQL(sqlCreateInc);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
			int versionNueva) {
		// NOTA: Por simplicidad del ejemplo aquí utilizamos directamente
		// la opción de eliminar la tabla anterior y crearla de nuevo
		// vacía con el nuevo formato.
		// Sin embargo lo normal será que haya que migrar datos de la
		// tabla antigua a la nueva, por lo que este método debería
		// ser más elaborado.
		// Se elimina la versión anterior de la tabla
		
		db.execSQL("DROP TABLE IF EXISTS Favoritos");
		db.execSQL("DROP TABLE IF EXISTS Gas");
		db.execSQL("DROP TABLE IF EXISTS Provincias");
		db.execSQL("DROP TABLE IF EXISTS Municipio");
		db.execSQL("DROP TABLE IF EXISTS Ini");
		// Se crea la nueva versión de la tabla
		db.execSQL(sqlCreate);
		db.execSQL(sqlCreateGas);
		db.execSQL(sqlCreateProv);
		db.execSQL(sqlCreateMun);
		db.execSQL(sqlCreateInc);
		
		
		
		
			
		
	}
}
