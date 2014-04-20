package usp.ime.movel.ouvidoria;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import usp.ime.movel.ouvidoria.device.SQLiteHelper;
import usp.ime.movel.ouvidoria.model.Incidente;
import usp.ime.movel.ouvidoria.web.HttpGetRequester;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;

public class IncidenteUpdater implements OnHttpResponseListener {

	private SQLiteHelper db;
	private List<Incidente> incidentes;
	private OnIncidenteUpdateListener listener;

	public IncidenteUpdater(Context context, OnIncidenteUpdateListener listener) {
		this.listener = listener;
		db = new SQLiteHelper(context);
	}
	
	public void checkForUpdates() {
		updateIncidentes();
		new HttpGetRequester().asyncGet(
				"http://uspservices.deusanyjunior.dj/incidente/"
						+ (incidentes.size() + 1) + ".json", this);
	}

	@Override
	public void onHttpResponse(JSONObject response) {
		JSONArray jsons = null;
		try {
			jsons = response.getJSONArray("incidentrecordlist");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsons != null)
			try {
				for (int i = 0; i < jsons.length(); i++) {
					Incidente.fromJSONObject(
							jsons.getJSONObject(i).getJSONObject(
									"incidentrecord")).makeCache(db,
							Incidente.STORED_INCIDENT_TABLE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		updateIncidentes();
	}
	
	public List<Incidente> getIncidentes() {
		return this.incidentes;
	}

	private void updateIncidentes() {
		incidentes = db.getAllIncidents(Incidente.STORED_INCIDENT_TABLE);
		listener.onIncidenteUpdate();
	}

	public void cleanCache() {
		db.resetTables();
	}

}
