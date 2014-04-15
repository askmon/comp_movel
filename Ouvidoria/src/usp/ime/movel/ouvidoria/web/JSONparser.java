package usp.ime.movel.ouvidoria.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONparser {

	static JSONObject jObj = null;
	static String json = "";

	InputStream is;

	public JSONparser(InputStream is) {
		this.is = is;
	}

	public JSONObject parse() {

		try {
			if (is == null)
				Log.e("JSONParser", "Input stream is null");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (IOException e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
			e.printStackTrace();
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "text=" + json);
			e.printStackTrace();
		}

		// return JSON String
		return jObj;
	}

}
