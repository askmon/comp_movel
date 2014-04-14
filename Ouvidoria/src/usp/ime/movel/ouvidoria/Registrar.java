package usp.ime.movel.ouvidoria;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
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

public class Registrar extends Activity implements OnClickListener {

	private Intent intent;
	private String username;
	private Button mPicture;
	private Button mGPS;
	private Button mEnviar;
	private EditText description;
	private EditText localization;
	private float latitude = 0;
	private float longitude = 0;
	private String file64;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registrar);
		intent = getIntent();
		username = intent.getStringExtra("uspid");
		TextView user = (TextView)findViewById(R.id.textView1);
		user.setText("Usuário: " + username);
		description = (EditText) findViewById(R.id.description);
		localization = (EditText) findViewById(R.id.location);
		mPicture = (Button) findViewById(R.id.picture);
		mPicture.setOnClickListener(this);
		mGPS = (Button) findViewById(R.id.gps);
		mGPS.setOnClickListener(this);
		mEnviar = (Button) findViewById(R.id.enviar);
		mEnviar.setOnClickListener(this);
	}

	public void getLocation(){
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
    	    public void onLocationChanged(Location location) {
    	    	Double latPoint = location.getLatitude();
    	    	Double longiPoint = location.getLongitude();
    	    	latitude = latPoint.floatValue();
    	    	longitude = longiPoint.floatValue();
    			TextView lat = (TextView)findViewById(R.id.lat);
    			lat.setText("Latitude: " + latPoint.toString());
    			TextView longi = (TextView)findViewById(R.id.longi);
    			longi.setText("Longitude: " + longiPoint.toString());
    	    }

    	    public void onStatusChanged(String provider, int status, Bundle extras) {}

    	    public void onProviderEnabled(String provider) {}

    	    public void onProviderDisabled(String provider) {}
    	  };

    	  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.picture:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File f = new File(android.os.Environment.getExternalStorageDirectory(), "ouvidoria.jpg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			System.out.println("Primeiro " + Uri.fromFile(f));
			startActivityForResult(intent, 1);
			break;
			
		case R.id.gps:
			Toast.makeText(Registrar.this, "Obtendo localização", Toast.LENGTH_LONG).show();
			getLocation();
			break;
			
		case R.id.enviar:
			Toast.makeText(Registrar.this, "Enviando", Toast.LENGTH_LONG).show();
			new AttemptSend().execute();
			break;

		default:
			break;
		}
	}
	

	private String convertTo64(File f) throws IOException{
		InputStream is = new FileInputStream(f);
		 
	    long length = f.length();
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }
	    byte[] bytes = new byte[(int)length];
	    
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }
 
	    is.close();
		byte[] encoded = Base64.encode(bytes, 0);
		String encodedString = new String(encoded);
 
		return encodedString;
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
                    if (bm == null) System.out.println("bm ta null");
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

	class AttemptSend extends AsyncTask<String, String, HttpResponse> {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected HttpResponse doInBackground(String... args) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("user", username);
				obj.put("description", description.getText().toString());
				obj.put("localization", localization.getText().toString());
				obj.put("photo", file64);
			} catch (JSONException e2) {
				e2.printStackTrace();
			}
			JSONObject obj2 = new JSONObject();
			try {
				obj2.put("incidentrecord", obj);
			} catch (JSONException e2) {
				e2.printStackTrace();
			}
			Log.d("Sending to server", obj2.toString());
			
			DefaultHttpClient httpclient = new DefaultHttpClient();

		    //url with the post data
		    HttpPost httpost = new HttpPost("http://uspservices.deusanyjunior.dj/incidente");

		    //passes the results to a string builder/entity
		    StringEntity se = null;
			try {
				se = new StringEntity(obj2.toString());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    //sets the post request as the resulting string
		    httpost.setEntity(se);
		    //sets a request header so the page receving the request
		    //will know what to do with it
		    httpost.setHeader("Accept", "application/json");
		    httpost.setHeader("Content-type", "application/json");

		    //Handles what is returned from the page 
		    ResponseHandler responseHandler = new BasicResponseHandler();
		    try {
				httpclient.execute(httpost, responseHandler);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(HttpResponse resultMessage) {
			Toast.makeText(Registrar.this, "Enviado", Toast.LENGTH_LONG).show();
		}
	}
}
