package usp.ime.movel.ouvidoria.device;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import usp.ime.movel.ouvidoria.model.Incidente;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 6;
	// Database Name
	private static final String DATABASE_NAME = "OuvidoriaDB";
	// Table Name
	private static final String INCIDENT_TABLES[] = { "pending_incidents",
			"stored_incidents" };

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create book table
		String middleQueryCode = "";

		IncidentKey[] keys = IncidentKey.values();
		for (int i = 0; i < keys.length; i++) {
			middleQueryCode += keys[i].getColumnName() + " "
					+ keys[i].getType();
			if (i + 1 < keys.length)
				middleQueryCode += ", ";
			else
				middleQueryCode += " )";
		}
		for (String tableName : INCIDENT_TABLES) {
			String queryCode = "CREATE TABLE " + tableName + " ( "
					+ middleQueryCode;
			Log.d("DB QUERY", queryCode);
			db.execSQL(queryCode);
		}
	}

	public void resetTables(SQLiteDatabase db) {
		for (String tableName : INCIDENT_TABLES)
			db.execSQL("DROP TABLE IF EXISTS " + tableName);
		// create fresh books table
		this.onCreate(db);
	}

	public void resetTables() {
		resetTables(getWritableDatabase());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older tables if existed
		if (oldVersion <= 4)
			db.execSQL("DROP TABLE IF EXISTS incidents");
		resetTables(db);
	}

	// --------------------------------------------------------------------------

	enum IncidentKey {
		ID("INTEGER PRIMARY KEY AUTOINCREMENT"), USPID("TEXT"), USERNAME("TEXT"), STATUS(
				"TEXT"), LATITUDE("DOUBLE"), LONGITUDE("DOUBLE"), FILE64("TEXT"), DESCRIPTION(
				"TEXT"), LOCALIZATION("TEXT");
		private String columnName;
		private String type;
		private Method getter;
		private Method setter;

		IncidentKey(String type) {
			this.columnName = this.name().toLowerCase();
			this.type = type;
			for (Method method : Incidente.class.getMethods()) {
				if (("get" + this.columnName).equals(method.getName()
						.toLowerCase()))
					getter = method;
				if (("set" + this.columnName).equals(method.getName()
						.toLowerCase()))
					setter = method;
			}
			if (getter == null)
				Log.e("IncidentKey", "Not found: " + "get" + this.columnName);
			if (setter == null)
				Log.e("IncidentKey", "Not found: " + "set" + this.columnName);
		}

		String getColumnName() {
			return this.columnName;
		}

		String getType() {
			return this.type;
		}

		Object get(Incidente incidente) {
			try {
				return getter.invoke(incidente);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		void set(Incidente incidente, Object value) {
			try {
				setter.invoke(incidente, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Get All Books
	public List<Incidente> getAllIncidents(int table) {

		List<Incidente> incidents = new ArrayList<Incidente>();

		// 1. build the query
		String query = "SELECT  * FROM " + INCIDENT_TABLES[table];

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. go over each row, build book and add it to list
		Incidente incident = null;
		if (cursor.moveToFirst()) {
			do {
				incident = new Incidente();
				IncidentKey[] keys = IncidentKey.values();
				for (int i = 0; i < keys.length; i++) {
					if (cursor.isNull(i))
						continue;
					else if (keys[i].getType() == "TEXT")
						keys[i].set(incident, cursor.getString(i));
					else if (keys[i].getType().startsWith("INTEGER"))
						keys[i].set(incident, cursor.getLong(i));
					else if (keys[i].getType() == "DOUBLE")
						keys[i].set(incident, cursor.getDouble(i));
				}
				incidents.add(incident);
			} while (cursor.moveToNext());
		}

		Log.d("getAllIncidents()", incidents.toString());

		return incidents;
	}

	private ContentValues makeValues(Incidente incident) {
		ContentValues values = new ContentValues();
		IncidentKey[] keys = IncidentKey.values();
		for (int i = 1; i < keys.length; i++) {
			if (keys[i].getType() == "TEXT")
				values.put(keys[i].getColumnName(),
						(String) keys[i].get(incident));
			else if (keys[i].getType().startsWith("INTEGER"))
				values.put(keys[i].getColumnName(),
						(Integer) keys[i].get(incident));
			else if (keys[i].getType() == "DOUBLE")
				values.put(keys[i].getColumnName(),
						(Double) keys[i].get(incident));
		}
		return values;
	}

	public Long addIncident(Incidente incident, int table) {
		long id = -1;
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		// 2. insert
		id = db.insert(INCIDENT_TABLES[table], // table
				null, // nullColumnHack
				makeValues(incident)); // key/value -> keys = column names/
										// values = column values
		// 3. close
		db.close();
		Log.d("addIncident()", Long.toString(id));
		return Long.valueOf(id);
	}

	public void updateIncident(Incidente incident, int table) {
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		// 3. updating row
		db.update(INCIDENT_TABLES[table], // table
				makeValues(incident), // column/value
				IncidentKey.ID.getColumnName() + " = ?", // selections
				new String[] { String.valueOf(incident.getId()) }); // selection
																	// args
		// 4. close
		db.close();
		Log.d("updateIncident()", incident.getId().toString());
	}

	public void removeIncident(Incidente incidente, int table) {
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		// 2. delete
		db.delete(INCIDENT_TABLES[table], IncidentKey.ID.getColumnName()
				+ " = ?", new String[] { String.valueOf(incidente.getId()) });
		// 3. close
		db.close();
	}

}
