package usp.ime.movel.ouvidoria.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

	static JSONObject jObj = null;
	static String json = "";

	InputStream is;

	public JSONParser(InputStream is) {
		this.is = is;
	}

	public JSONObject parse() {

		try {
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
			e.printStackTrace();
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// return JSON String
		return jObj;
	}

}
