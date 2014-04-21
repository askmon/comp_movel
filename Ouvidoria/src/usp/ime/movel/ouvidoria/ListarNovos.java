package usp.ime.movel.ouvidoria;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONObject;

import usp.ime.movel.ouvidoria.model.Incidente;
import usp.ime.movel.ouvidoria.web.HttpGetRequester;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListarNovos extends OuvidoriaActivity implements OnClickListener,
		OnIncidenteUpdateListener, OnHttpResponseListener {

	private String status;
	private int pageNumber;
	private TextView[] incidentTexts;
	private ImageView[] incidentImages;
	private Long[] ids = new Long[5];
	private IncidenteUpdater updater;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listarnovos);
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
		((Button) findViewById(R.id.clean_cache)).setOnClickListener(this);
		updater = new IncidenteUpdater(this, this);
		updater.checkForUpdates();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater m = getMenuInflater();
		m.inflate(R.menu.context, menu);
		id = v.getId();
	}

	@Override
	public void onHttpResponse(JSONObject response) {
		return; // TODO verificar melhor a resposta?
	}

	@Override
	public void onIncidenteUpdate() {
		TextView stats = (TextView) findViewById(R.id.textView1);
		stats.setText("Incidentes " + (5 * pageNumber + 1) + " a "
				+ (5 * (pageNumber + 1)));
		List<Incidente> incidentes = updater.getIncidentes();
		for (int i = 0; i < 5; i++) {
			int index = 5 * pageNumber + i;
			if (index >= incidentes.size()) {
				incidentTexts[i].setText(null);
				incidentImages[i].setImageBitmap(null);
				ids[i] = null;
			} else {
				Incidente incidente = incidentes.get(index);
				incidentTexts[i].setText(makeIncidentText(incidente));
				ids[i] = incidente.getId();
				incidentImages[i].setImageBitmap(bitmapFromFile(
						incidente.getFile64(), index));
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.aberto:
			switch (id) {
			case R.id.ocorrencia1:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[0] + "&status=Aberto", this);
				break;
			case R.id.ocorrencia2:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[1] + "&status=Aberto", this);
				break;
			case R.id.ocorrencia3:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[2] + "&status=Aberto", this);
				break;
			case R.id.ocorrencia4:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[3] + "&status=Aberto", this);
				break;
			case R.id.ocorrencia5:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[4] + "&status=Aberto", this);
				break;
			default:
				break;
			}
			break;
		case R.id.andamento:
			switch (id) {
			case R.id.ocorrencia1:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[0] + "&status=Andamento", this);
				break;
			case R.id.ocorrencia2:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[1] + "&status=Andamento", this);
				break;
			case R.id.ocorrencia3:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[2] + "&status=Andamento", this);
				break;
			case R.id.ocorrencia4:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[3] + "&status=Andamento", this);
				break;
			case R.id.ocorrencia5:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[4] + "&status=Andamento", this);
				break;
			default:
				break;
			}
			break;
		case R.id.oculto:
			switch (id) {
			case R.id.ocorrencia1:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[0] + "&status=Oculto", this);
				System.out
						.println("http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[0] + "&status=Oculto");
				break;
			case R.id.ocorrencia2:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[1] + "&status=Oculto", this);
				break;
			case R.id.ocorrencia3:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[2] + "&status=Oculto", this);
				break;
			case R.id.ocorrencia4:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[3] + "&status=Oculto", this);
				break;
			case R.id.ocorrencia5:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[4] + "&status=Oculto", this);
				break;
			default:
				break;
			}
			break;
		case R.id.resolvido:
			switch (id) {
			case R.id.ocorrencia1:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[0] + "&status=Resolvido", this);
				System.out.println("Entrou onde n devia");
				break;
			case R.id.ocorrencia2:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[1] + "&status=Resolvido", this);
				break;
			case R.id.ocorrencia3:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[2] + "&status=Resolvido", this);
				break;
			case R.id.ocorrencia4:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[3] + "&status=Resolvido", this);
				break;
			case R.id.ocorrencia5:
				new HttpGetRequester().asyncGet(
						"http://uspservices.deusanyjunior.dj/incidentrecords/set_status.json?id="
								+ ids[4] + "&status=Resolvido", this);
				break;
			default:
				break;
			}
			break;
		default:
			break;

		}

		return super.onContextItemSelected(item);
	}

	private String makeIncidentText(Incidente incidente) {
		return "----------\n" + "Descrição: " + incidente.getDescription()
				+ "\n" + "Local: " + incidente.getLocalization() + "\n"
				+ "Status: " + incidente.getStatus() + "\n";
	}

	//Recebe uma string de um binario em Base64 e devolve o arquivo
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

	//Recebe um arquivo e devolve o bitmap para poder exibir na tela
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
			onIncidenteUpdate();
			break;
		case R.id.right:
			pageNumber = Math.min(pageNumber + 1, updater.getIncidentes()
					.size() / 5);
			onIncidenteUpdate();
			break;
		case R.id.clean_cache:
			updater.cleanCache();
			onIncidenteUpdate();
			Toast.makeText(this, "Obtendo incidentes, aguarde...",
					Toast.LENGTH_LONG).show();
			updater.checkForUpdates();
			break;
		default:
			break;
		}
	}
}
