package usp.ime.movel.ouvidoria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import usp.ime.movel.ouvidoria.web.HttpGetRequest;
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
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().
					findFragmentById(R.id.map)).getMap();
        }
		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		new HttpGetRequest(this).execute("http://uspservices.deusanyjunior.dj/incidente/1.json");

		

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
			
		double[] latitude = new double[10];
		double[] longitude = new double[10];
		String[] desc = new String[10];
		for(int i = 0; i < 10; i++){
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
         Marker M1 = googleMap.addMarker(new MarkerOptions().
         position(new LatLng(latitude[0], longitude[0])).title(desc[0]));
         Marker M2 = googleMap.addMarker(new MarkerOptions().
                 position(new LatLng(latitude[1], longitude[1])).title(desc[1]));
         Marker M3 = googleMap.addMarker(new MarkerOptions().
                 position(new LatLng(latitude[2], longitude[2])).title(desc[2]));
         Marker M4 = googleMap.addMarker(new MarkerOptions().
                 position(new LatLng(latitude[3], longitude[3])).title(desc[3]));
         Marker M5 = googleMap.addMarker(new MarkerOptions().
                 position(new LatLng(latitude[4], longitude[4])).title(desc[4]));
         Marker M6 = googleMap.addMarker(new MarkerOptions().
                 position(new LatLng(latitude[5], longitude[5])).title(desc[5]));
         Marker M7 = googleMap.addMarker(new MarkerOptions().
                 position(new LatLng(latitude[6], longitude[6])).title(desc[6]));
         Marker M8 = googleMap.addMarker(new MarkerOptions().
                 position(new LatLng(latitude[7], longitude[7])).title(desc[7]));
         Marker M9 = googleMap.addMarker(new MarkerOptions().
                 position(new LatLng(latitude[8], longitude[8])).title(desc[8]));
         Marker M10 = googleMap.addMarker(new MarkerOptions().
                 position(new LatLng(latitude[9], longitude[9])).title(desc[9]));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
