package usp.ime.movel.ouvidoria;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.web.HttpGetRequest;
import usp.ime.movel.ouvidoria.web.InsecureHttpClientFactory;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ListarNovos extends OuvidoriaActivity implements OnClickListener,
		OnHttpResponseListener {

	private Intent intent;
	private String status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listarnovos);
		intent = getIntent();
		status = "Carregando";
		TextView stats = (TextView) findViewById(R.id.textView1);
		stats.setText(status);
		new HttpGetRequest(new InsecureHttpClientFactory(), this)
		.execute("http://uspservices.deusanyjunior.dj/incidente/1.json");
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
		TextView stats = (TextView) findViewById(R.id.textView1);
		stats.setText("");
		TextView oc1 = (TextView) findViewById(R.id.ocorrencia1);
		TextView oc2 = (TextView) findViewById(R.id.ocorrencia2);
		TextView oc3 = (TextView) findViewById(R.id.ocorrencia3);
		TextView oc4 = (TextView) findViewById(R.id.ocorrencia4);
		TextView oc5 = (TextView) findViewById(R.id.ocorrencia5);
		ImageView im1 = (ImageView)findViewById(R.id.ivImage1);
		ImageView im2 = (ImageView)findViewById(R.id.ivImage2);
		ImageView im3 = (ImageView)findViewById(R.id.ivImage3);
		ImageView im4 = (ImageView)findViewById(R.id.ivImage4);
		ImageView im5 = (ImageView)findViewById(R.id.ivImage5);
		oc1.setText(parseJSON(jsons, 0));
		oc2.setText(parseJSON(jsons, 1));
		oc3.setText(parseJSON(jsons, 2));
		oc4.setText(parseJSON(jsons, 3));
		oc5.setText(parseJSON(jsons, 4));
	}

	private String parseJSON(JSONArray array, int index){
		try {
			return "Descrição: " + array.getJSONObject(index).getJSONObject("incidentrecord").getString("description") + "\n" + "Local: " + array.getJSONObject(index).getJSONObject("incidentrecord").getString("localization") + "\n";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private File FileFrom64(String file64, String filename){
		byte[] btDataFile = Base64.decode(file64, 0);
		File f = new File(filename);
		FileOutputStream osf = null;
		try {
			osf = new FileOutputStream(f);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			osf.write(btDataFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			osf.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}


}
