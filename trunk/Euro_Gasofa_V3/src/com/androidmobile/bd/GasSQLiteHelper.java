package com.androidmobile.bd;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import util.utility;

import com.androidmobile.JR.MainActivity;
import com.androidmobile.model.Provincia;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GasSQLiteHelper extends SQLiteOpenHelper {

	// Sentencia SQL para crear la tabla de Usuarios
	String sqlCreate = "CREATE TABLE Favoritos (pos INTEGER, nombre TEXT)";
	String sqlCreateGas = "Create table Gas ( UPV TEXT,fecha TEXT, nombre TEXT,horario TEXT,direccion TEXT, localidad TEXT,provincia TEXT,gasolina95 TEXT,gasolina98 TEXT,gasoleo TEXT)";

	String sqlCreateProv = "CREATE TABLE Provincias (id_provincia TEXT, nombre_provincia TEXT)";
	public GasSQLiteHelper(Context contexto, String nombre,
			CursorFactory factory, int version) {
	
		super(contexto, nombre, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Se ejecuta la sentencia SQL de creaci�n de la tabla
		db.execSQL(sqlCreate);
		db.execSQL(sqlCreateGas);
		
		db.execSQL(sqlCreateProv);		
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
			int versionNueva) {
		// NOTA: Por simplicidad del ejemplo aqu� utilizamos directamente
		// la opci�n de eliminar la tabla anterior y crearla de nuevo
		// vac�a con el nuevo formato.
		// Sin embargo lo normal ser� que haya que migrar datos de la
		// tabla antigua a la nueva, por lo que este m�todo deber�a
		// ser m�s elaborado.
		// Se elimina la versi�n anterior de la tabla
		
		db.execSQL("DROP TABLE IF EXISTS Favoritos");
		db.execSQL("DROP TABLE IF EXISTS Gas");
		db.execSQL("DROP TABLE IF EXISTS Provincias");
		// Se crea la nueva versi�n de la tabla
		db.execSQL(sqlCreate);
		db.execSQL(sqlCreateGas);
		db.execSQL(sqlCreateProv);
		
		
		
		
			
		
	}
}