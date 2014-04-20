package usp.ime.movel.ouvidoria;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.device.SQLiteHelper;
import usp.ime.movel.ouvidoria.model.Incidente;
import usp.ime.movel.ouvidoria.web.HttpGetRequester;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ListarNovos extends OuvidoriaActivity implements OnClickListener,
		OnHttpResponseListener {

	private Intent intent;
	private String status;
	private int pageNumber;
	private TextView[] incidentTexts;
	private ImageView[] incidentImages;
	private Long[] ids = new Long[5]; 
	private SQLiteHelper db;
	private List<Incidente> incidentes;

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
		registerForContextMenu(incidentTexts[0]);  
		registerForContextMenu(incidentTexts[1]);
		registerForContextMenu(incidentTexts[2]);
		registerForContextMenu(incidentTexts[3]);
		registerForContextMenu(incidentTexts[4]);
		((Button) findViewById(R.id.left)).setOnClickListener(this);
		((Button) findViewById(R.id.right)).setOnClickListener(this);
		db = new SQLiteHelper(this);
		updateIncidentes();
		new HttpGetRequester().asyncGet(
				"http://uspservices.deusanyjunior.dj/incidente/"
						+ (incidentes.size() + 1) + ".json", this);
	}

	 @Override  
	 public void onCreateContextMenu(ContextMenu menu, View v,  
	     ContextMenuInfo menuInfo) {  
	     // TODO Auto-generated method stub  
	     super.onCreateContextMenu(menu, v, menuInfo);  
	     MenuInflater m = getMenuInflater();  
	     m.inflate(R.menu.context, menu);  
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

	private void updateIncidentes() {
		incidentes = db.getAllIncidents(Incidente.STORED_INCIDENT_TABLE);
		updateList();
	}

	private void updateList() {
		TextView stats = (TextView) findViewById(R.id.textView1);
		stats.setText("Incidentes " + (5 * pageNumber + 1) + " a "
				+ (5 * (pageNumber + 1)));

		for (int i = 0; i < 5; i++) {
			int index = 5 * pageNumber + i;
			if (index >= incidentes.size())
				break;
			Incidente incidente = incidentes.get(index);
			incidentTexts[i].setText(makeIncidentText(incidente));
			ids[i] = incidente.getId();
			incidentImages[i].setImageBitmap(bitmapFromFile(
					incidente.getFile64(), index));
		}
	}

	private String makeIncidentText(Incidente incidente) {
		return "----------\n" + "Descrição: " + incidente.getDescription() + "\n" + "Local: "
				+ incidente.getLocalization() + "\n" + "Status: "
						+ incidente.getStatus() + "\n";
	}

	private File fileFrom64(String file64, String filename) {
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

	private Bitmap bitmapFromFile(String data, int index) {
		File f = fileFrom64(data, Environment.getExternalStorageDirectory()
				.toString() + "/ouvidoria" + index + ".jpg");
		try {
			Bitmap bm;
			bm = BitmapFactory.decodeFile(f.getAbsolutePath());
			if (bm == null)
				System.out.println("bm ta null");
			else
				bm = Bitmap.createScaledBitmap(bm, 140, 140, true);
			return bm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left:
			pageNumber = Math.max(pageNumber - 1, 0);
			break;
		case R.id.right:
			pageNumber = Math.min(pageNumber + 1, incidentes.size() / 5);
			break;
		default:
			break;
		}
		updateList();
	}
}
