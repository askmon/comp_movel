package usp.ime.movel.ouvidoria.device;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import usp.ime.movel.ouvidoria.model.Incidente;
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
                incident.setId(Integer.parseInt(cursor.getString(0)));
                incident.setUspId(cursor.getString(1));
                incident.setUserName(cursor.getString(2));
                incident.setLatitude(Double.parseDouble(cursor.getString(3)));
                incident.setLongitude(Double.parseDouble(cursor.getString(4)));
                incident.setFile64(cursor.getString(5));
                incident.setDescription(cursor.getString(6));
                incident.setLocalization(cursor.getString(7));
                incidents.add(incident);
            } while (cursor.moveToNext());
        }
        
		Log.d("getAllIncidents()", incidents.toString());

        return incidents;
    }
	
}
