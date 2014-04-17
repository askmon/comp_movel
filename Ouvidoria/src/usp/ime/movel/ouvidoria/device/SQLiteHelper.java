package usp.ime.movel.ouvidoria.device;

import java.lang.reflect.Field;
import java.util.LinkedList;
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
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "OuvidoriaDB";
    // Table Name
    private static final String DATABASE_TABLE = "incidents";
    
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create book table
		String queryCode = "CREATE TABLE "+DATABASE_TABLE+" ( "+
				"id INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"uspid TEXT, "+
				"username TEXT, "+
				"latitude DOUBLE, "+
				"longitude DOUBLE, "+
				"file64 TEXT, "+
				"description TEXT, "+
				"localization TEXT )";
		Log.d("DB QUERY", queryCode);
		// create books table
		db.execSQL(queryCode);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        // create fresh books table
        this.onCreate(db);
	}
	
	//--------------------------------------------------------------------------
	
    private static final String KEY_ID = "id";
    private static final String KEY_USPID = "uspid";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_FILE64 = "file64";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_LOCALIZATION = "localization";
	
	// Get All Books
    public List<Incidente> getAllIncidents() {
        List<Incidente> incidents = new LinkedList<Incidente>();

        // 1. build the query
        String query = "SELECT  * FROM " + DATABASE_TABLE;
 
    	// 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        Incidente incident = null;
        if (cursor.moveToFirst()) {
            do {
            	incident = new Incidente();
                incident.setId(Long.parseLong(cursor.getString(0)));
                incident.setUspId(cursor.getString(1));
                incident.setUserName(cursor.getString(2));
                String temp;
                temp = cursor.getString(3);
                if (temp != null)
                	incident.setLatitude(Double.parseDouble(temp));
                temp = cursor.getString(4);
                if (temp != null)
                	incident.setLongitude(Double.parseDouble(temp));
                incident.setFile64(cursor.getString(5));
                incident.setDescription(cursor.getString(6));
                incident.setLocalization(cursor.getString(7));
                incidents.add(incident);
            } while (cursor.moveToNext());
        }
        
		Log.d("getAllIncidents()", incidents.toString());

        return incidents;
    }
    
    private ContentValues makeValues(Incidente incident) {
        ContentValues values = new ContentValues();
        values.put(KEY_USPID, incident.getUspId());
        values.put(KEY_USERNAME, incident.getUserName());
        values.put(KEY_LATITUDE, incident.getLatitude());
        values.put(KEY_LONGITUDE, incident.getLongitude());
        values.put(KEY_FILE64, incident.getFile64());
        values.put(KEY_DESCRIPTION, incident.getDescription());
        values.put(KEY_LOCALIZATION, incident.getLocalization());
        return values;
    }
    
    public Long addIncident(Incidente incident) {
    	long id = -1;
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
        // 2. insert
        id = db.insert(DATABASE_TABLE, // table
        		null, //nullColumnHack
        		makeValues(incident)); // key/value -> keys = column names/ values = column values
        db.close();
		Log.d("addIncident()", Long.toString(id));
    	return Long.valueOf(id);
    }
    
    public void updateIncident(Incidente incident) {
    	// 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 3. updating row
        int i = db.update(DATABASE_TABLE, //table
        		makeValues(incident), // column/value
        		KEY_ID+" = ?", // selections
                new String[] { String.valueOf(incident.getId()) }); //selection args
        db.close();
		Log.d("updateIncident()", incident.getId().toString());
    }
	
}
