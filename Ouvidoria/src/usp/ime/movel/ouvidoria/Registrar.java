package usp.ime.movel.ouvidoria;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.web.HttpEntityProvider;
import usp.ime.movel.ouvidoria.web.HttpPostRequest;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Registrar extends OuvidoriaActivity implements OnClickListener,
		OnHttpResponseListener {

	private Intent intent;
	private String username;
	private Button mPicture;
	private Button mGPS;
	private Button mEnviar;
	private EditText description;
	private EditText localization;
	private Double latitude;
	private Double longitude;
	private String file64;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registrar);
		intent = getIntent();
		username = intent.getStringExtra("uspid");
		TextView user = (TextView) findViewById(R.id.textView1);
		user.setText("Usuário: " + username);
		description = (EditText) findViewById(R.id.description);
		localization = (EditText) findViewById(R.id.location);
		file64 = null;
		latitude = null;
		longitude = null;
		mPicture = (Button) findViewById(R.id.picture);
		mPicture.setOnClickListener(this);
		mGPS = (Button) findViewById(R.id.gps);
		mGPS.setOnClickListener(this);
		mEnviar = (Button) findViewById(R.id.enviar);
		mEnviar.setOnClickListener(this);
	}

	public void getLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				TextView lat = (TextView) findViewById(R.id.lat);
				lat.setText("Latitude: " + latitude.toString());
				TextView longi = (TextView) findViewById(R.id.longi);
				longi.setText("Longitude: " + longitude.toString());
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.picture:
			if (getBatteryState().getLevel() > 15) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File f = new File(
						android.os.Environment.getExternalStorageDirectory(),
						"ouvidoria.jpg");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				System.out.println("Primeiro " + Uri.fromFile(f));
				startActivityForResult(intent, 1);
			} else {
				Toast.makeText(Registrar.this, "Pouca bateria",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.gps:
			if (getBatteryState().getLevel() > 15) {
				Toast.makeText(Registrar.this, "Obtendo localização",
						Toast.LENGTH_LONG).show();
				getLocation();
			} else {
				Toast.makeText(Registrar.this, "Pouca bateria",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.enviar:
			if (!getConnectionState().isConnected())
				Toast.makeText(Registrar.this, "Sem conexão", Toast.LENGTH_LONG)
						.show();
			else if (getBatteryState().getLevel() <= 15
					&& getConnectionState().getType() != "WIFI") {
				Toast.makeText(Registrar.this, "Pouca bateria e sem WIFI",
						Toast.LENGTH_LONG).show();
			} else {
				// TODO validar campos
				Toast.makeText(Registrar.this, "Enviando", Toast.LENGTH_LONG)
						.show();
				HttpEntityProvider provider = new HttpEntityProvider() {

					public AbstractHttpEntity provideEntity() {
						try {
							return new StringEntity(makeJSONRequest()
									.toString());
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						return null;
					}

					public boolean hasContentType() {
						return true;
					}

					public String getContentType() {
						return "application/json";
					}
				};
				new HttpPostRequest(provider, this)
						.execute("http://uspservices.deusanyjunior.dj/incidente");
			}
			break;

		default:
			break;
		}
	}

	private String convertTo64(File f) throws IOException {
		InputStream is = new FileInputStream(f);

		long length = f.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		is.close();
		byte[] encoded = Base64.encode(bytes, 0);
		String encodedString = new String(encoded);

		return encodedString;
	}

	@Override
	public void onHttpResponse(JSONObject response) {
		if (response == null) {
			Toast.makeText(Registrar.this, "Falha", Toast.LENGTH_LONG).show();
			return;
		}
		Toast.makeText(Registrar.this, "Enviado", Toast.LENGTH_LONG).show();
		Log.d("Resposta do Incidente", response.toString());
	}

	private JSONObject makeJSONRequest() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("user", username);
			// obj.put("login", login); // TODO
			obj.put("description", description.getText().toString());
			obj.put("localization", localization.getText().toString());
			if (file64 != null)
				obj.put("photo", file64);
			if (latitude != null && longitude != null) {
				obj.put("latitude", latitude.doubleValue());
				obj.put("longitude", longitude.doubleValue());
			}
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		JSONObject obj2 = new JSONObject();
		try {
			obj2.put("incidentrecord", obj);
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		return obj2;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			File f = new File(Environment.getExternalStorageDirectory()
					.toString() + "/ouvidoria.jpg");
			try {
				file64 = convertTo64(f);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("Segundo " + Uri.fromFile(f));
			try {
				Bitmap bm;
				bm = BitmapFactory.decodeFile(f.getAbsolutePath());
				if (bm == null)
					System.out.println("bm ta null");
				System.out.println(f.getAbsolutePath());
				bm = Bitmap.createScaledBitmap(bm, 140, 140, true);
				ImageView image = (ImageView) findViewById(R.id.ivImage);
				image.setImageBitmap(bm);

				f.delete();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
