package usp.ime.movel.ouvidoria;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.device.SQLiteHelper;
import usp.ime.movel.ouvidoria.model.Incidente;
import usp.ime.movel.ouvidoria.web.HttpGetRequester;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Mapa extends OuvidoriaActivity implements OnHttpResponseListener {

	private GoogleMap googleMap;
	private SQLiteHelper db;
	private List<Incidente> incidentes;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		Toast.makeText(Mapa.this, "Aguarde carregamento das ocorrências",
				Toast.LENGTH_LONG).show();
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
		}
		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		db = new SQLiteHelper(this);
		updateIncidentes();
		new HttpGetRequester().asyncGet(
				"http://uspservices.deusanyjunior.dj/incidente/"
						+ (incidentes.size() + 1) + ".json", this);
	}

	private void updateIncidentes() {
		incidentes = db.getAllIncidents(Incidente.STORED_INCIDENT_TABLE);
		updateMap();
	}

	private void updateMap() {
		googleMap.clear();
		for (Incidente incidente : incidentes) {
			if (incidente.getLatitude() != null) {
				googleMap.addMarker(new MarkerOptions().position(
						new LatLng(incidente.getLatitude(), incidente
								.getLongitude())).title(
						incidente.getDescription()));
			}
		}
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

}
