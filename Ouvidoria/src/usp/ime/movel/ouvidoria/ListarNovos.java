package usp.ime.movel.ouvidoria;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.web.HttpGetRequester;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ListarNovos extends OuvidoriaActivity implements OnClickListener,
		OnHttpResponseListener {

	private Intent intent;
	private String status;
	private int pageNumber;
	private TextView[] incidentTexts;
	private ImageView[] incidentImages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listarnovos);
		intent = getIntent();
		status = "Carregando";
		pageNumber = 0;
		TextView stats = (TextView) findViewById(R.id.textView1);
		stats.setText(status);
		incidentTexts = new TextView[5];
		incidentTexts[0] = (TextView) findViewById(R.id.ocorrencia1);
		incidentTexts[1] = (TextView) findViewById(R.id.ocorrencia2);
		incidentTexts[2] = (TextView) findViewById(R.id.ocorrencia3);
		incidentTexts[3] = (TextView) findViewById(R.id.ocorrencia4);
		incidentTexts[4] = (TextView) findViewById(R.id.ocorrencia5);
		incidentImages = new ImageView[5];
		incidentImages[0] = (ImageView) findViewById(R.id.ivImage1);
		incidentImages[1] = (ImageView) findViewById(R.id.ivImage2);
		incidentImages[2] = (ImageView) findViewById(R.id.ivImage3);
		incidentImages[3] = (ImageView) findViewById(R.id.ivImage4);
		incidentImages[4] = (ImageView) findViewById(R.id.ivImage5);
		new HttpGetRequester().asyncGet(
				"http://uspservices.deusanyjunior.dj/incidente/1.json", this);
	}

	@Override
	public void onHttpResponse(JSONObject response) {
		JSONArray jsons = null;
		try {
			jsons = response.getJSONArray("incidentrecordlist");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			TextView stats = (TextView) findViewById(R.id.textView1);
			stats.setText("Incidentes " + 5 * pageNumber + " a "
					+ (5 * (pageNumber + 1) - 1));
			for (int i = 0; i < 5; i++) {
				incidentTexts[i].setText(parseJSON(jsons, 5 * pageNumber + i));
				incidentImages[i].setImageBitmap(BitmapFromFile(jsons, 5
						* pageNumber + i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String parseJSON(JSONArray array, int index) {
		try {
			return "Descrição: "
					+ array.getJSONObject(array.length() - 1 - index)
							.getJSONObject("incidentrecord")
							.getString("description")
					+ "\n"
					+ "Local: "
					+ array.getJSONObject(array.length() - 1 - index)
							.getJSONObject("incidentrecord")
							.getString("localization") + "\n";
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private File FileFrom64(String file64, String filename) {
		byte[] btDataFile = Base64.decode(file64, 0);
		File f = new File(filename);
		FileOutputStream osf = null;
		try {
			osf = new FileOutputStream(f);
			osf.write(btDataFile);
			osf.flush();
			osf.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	private Bitmap BitmapFromFile(JSONArray array, int index) {
		String photo = null;
		try {
			photo = array.getJSONObject(array.length() - 1 - index)
					.getJSONObject("incidentrecord").getString("photo");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		File f = FileFrom64(photo, Environment.getExternalStorageDirectory()
				.toString() + "/ouvidoria" + index + ".jpg");
		try {
			Bitmap bm;
			bm = BitmapFactory.decodeFile(f.getAbsolutePath());
			if (bm == null)
				System.out.println("bm ta null");
			bm = Bitmap.createScaledBitmap(bm, 140, 140, true);
			return bm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onClick(View arg0) {

	}
}
