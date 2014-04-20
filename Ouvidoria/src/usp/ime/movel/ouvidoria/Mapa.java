package usp.ime.movel.ouvidoria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.web.HttpGetRequester;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.widget.Toast;



@SuppressLint("NewApi")
public class Mapa extends OuvidoriaActivity implements OnHttpResponseListener {
	private GoogleMap googleMap;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		Toast.makeText(Mapa.this, "Aguarde carregamento das ocorrências", Toast.LENGTH_LONG).show();
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().
					findFragmentById(R.id.map)).getMap();
        }
		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		new HttpGetRequester().asyncGet("http://uspservices.deusanyjunior.dj/incidente/1.json", this);

		

   }
	@Override
	public void onHttpResponse(JSONObject response) {
		// TODO Auto-generated method stub
		JSONArray jsons = null;
		try {
			jsons = response.getJSONArray("incidentrecordlist");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try { 
			
			double[] latitude = new double[jsons.length()];
			double[] longitude = new double[jsons.length()];
			String[] desc = new String[jsons.length()];
			for(int i = 0; i < jsons.length(); i++){
				String lat = jsons.getJSONObject(jsons.length() - 1 - i).getJSONObject("incidentrecord")
						.getString("latitude");
				if(lat.equals("null")) lat = "0";
				latitude[i] = Double.parseDouble(lat);
				String lon = jsons.getJSONObject(jsons.length() - 1 - i).getJSONObject("incidentrecord")
						.getString("longitude");
				if(lon.equals("null")) lon = "0";
				longitude[i] = Double.parseDouble(lon);
				desc[i] = jsons.getJSONObject(jsons.length() - 1 - i).getJSONObject("incidentrecord")
						.getString("description");
			}
			for(int j = 0; j < jsons.length(); j++){
		         if(latitude[j] != 0){
		         googleMap.addMarker(new MarkerOptions().
		                 position(new LatLng(latitude[j], longitude[j])).title(desc[j]));
		         }
			}
		}
		catch (Exception e) {
				e.printStackTrace();
		}
	}

}
