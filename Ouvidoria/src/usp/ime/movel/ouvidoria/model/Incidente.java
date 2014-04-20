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

	public Incidente() {
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

	public void makeCache(SQLiteHelper db) {
		if (id == null)
			id = db.addIncident(this, SQLiteHelper.PENDING_INCIDENT_TABLE);
		else
			db.updateIncident(this, SQLiteHelper.PENDING_INCIDENT_TABLE);
	}
	
	public void cleanCache(SQLiteHelper db) {
		if (id != null)
			db.removeIncident(this, SQLiteHelper.PENDING_INCIDENT_TABLE);
	}

	@Override
	public String toString() {
		return "[Incidente "+this.id+": "+this.description+"]";
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