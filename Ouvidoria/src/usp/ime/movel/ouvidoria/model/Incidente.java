package usp.ime.movel.ouvidoria.model;

import org.json.JSONException;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.device.SQLiteHelper;

public class Incidente {
	private Long id = null;
	private String uspId = null;
	private String userName = null;
	private Double latitude = null;
	private Double longitude = null;
	private String file64 = null;
	private String description = null;
	private String localization = null;
	final public static int PENDING_INCIDENT_TABLE = 0;
	final public static int STORED_INCIDENT_TABLE = 1;

	public Incidente() {
	}

	private static Double extractDouble(JSONObject jsonObject, String name) {
		String inString = jsonObject.optString(name, null);
		if (inString != null && !inString.equals("null"))
			return Double.parseDouble(inString);
		return null;
	}

	public static Incidente fromJSONObject(JSONObject jsonObject) {
		Incidente incidente = new Incidente();
		try {
			incidente.setUspId(jsonObject.getString("user"));
			incidente.setUserName(jsonObject.getString("login"));
			incidente.setDescription(jsonObject.getString("description"));
			incidente.setLocalization(jsonObject.getString("localization"));
			if (jsonObject.has("photo"))
				incidente.setFile64(jsonObject.optString("photo", null));
			incidente.setLatitude(extractDouble(jsonObject, "latitude"));
			incidente.setLongitude(extractDouble(jsonObject, "longitude"));
			return incidente;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject toJSONObject() {
		JSONObject record = new JSONObject();
		try {
			record.put("user", this.getUspId());
			record.put("login", this.getUserName());
			record.put("description", this.description);
			record.put("localization", this.localization);
			if (this.getFile64() != null)
				record.put("photo", this.getFile64());
			if (this.getLatitude() != null && this.getLongitude() != null) {
				record.put("latitude", this.getLatitude().doubleValue());
				record.put("longitude", this.getLongitude().doubleValue());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject result = new JSONObject();
		try {
			result.put("incidentrecord", record);
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void makeCache(SQLiteHelper db, int table) {
		if (id == null)
			id = db.addIncident(this, table);
		else
			db.updateIncident(this, table);
	}

	public void cleanCache(SQLiteHelper db, int table) {
		if (id != null)
			db.removeIncident(this, table);
	}

	@Override
	public String toString() {
		return "[Incidente " + this.id + ": " + this.description + "]";
	}

	public String getUspId() {
		return uspId;
	}

	public void setUspId(String uspID) {
		this.uspId = uspID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getFile64() {
		return file64;
	}

	public void setFile64(String file64) {
		this.file64 = file64;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocalization() {
		return localization;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}