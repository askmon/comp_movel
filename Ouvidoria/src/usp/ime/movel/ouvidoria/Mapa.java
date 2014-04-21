package usp.ime.movel.ouvidoria;

import usp.ime.movel.ouvidoria.model.Incidente;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Mapa extends OuvidoriaActivity implements
		OnIncidenteUpdateListener {

	private GoogleMap googleMap;
	private IncidenteUpdater updater;
	
	public Mapa(GoogleMap googleMap) {
		super();
		this.googleMap = googleMap;
	}
	
	public Mapa() {
		super();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("VEM PRA CÁ********************************************************");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		Toast.makeText(Mapa.this, "Aguarde carregamento das ocorrências",
				Toast.LENGTH_LONG).show();
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			System.out.println("NUUUUULL **************************");
		}
		if (googleMap == null) {
			System.out.println("CONTINUA NUUUUUUUUUUUUUUUUULLLL************************");
			System.out.println(((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap() + "------------------------------------------");
		}
		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		updater = new IncidenteUpdater(this, this);
		updater.checkForUpdates();
	}

	@Override
	public void onIncidenteUpdate() {
		googleMap.clear();
		for (Incidente incidente : updater.getIncidentes()) {
			if (incidente.getLatitude() != null) {
				googleMap.addMarker(new MarkerOptions().position(
						new LatLng(incidente.getLatitude(), incidente
								.getLongitude())).title(
						incidente.getDescription()));
			}
		}
	}

}
