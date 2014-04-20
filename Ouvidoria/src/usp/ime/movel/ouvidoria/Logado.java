package usp.ime.movel.ouvidoria;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.device.SQLiteHelper;
import usp.ime.movel.ouvidoria.model.Incidente;
import usp.ime.movel.ouvidoria.web.HttpEntityProvider;
import usp.ime.movel.ouvidoria.web.HttpPostRequester;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Logado extends OuvidoriaActivity implements OnClickListener,
		OnHttpResponseListener {

	private Intent intent;
	private String userName;
	private String uspID;
	private Button mRegister;
	private TextView mIncidentCounter;
	private SQLiteHelper db;
	private Incidente incidentePendendo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logado);
		intent = getIntent();
		userName = intent.getStringExtra("username");
		uspID = intent.getStringExtra("uspid");
		TextView user = (TextView) findViewById(R.id.textView1);
		user.setText("Usuário: " + userName);
		mRegister = (Button) findViewById(R.id.register);
		mRegister.setOnClickListener(this);
		mIncidentCounter = (TextView) findViewById(R.id.incidentCounter);
		db = new SQLiteHelper(this);
		incidentePendendo = null;
	}

	private void checkIncidents() {
		List<Incidente> incidentes = db.getAllIncidents(SQLiteHelper.PENDING_INCIDENT_TABLE);
		mIncidentCounter.setText("Envios pendentes: " + incidentes.size());
		if (incidentePendendo != null || incidentes.isEmpty() || !isOkToSend())
			return;
		incidentePendendo = incidentes.get(0);
		Toast.makeText(Logado.this, "Enviando pendências", Toast.LENGTH_LONG)
				.show();
		Log.d("Enviando Incidente", incidentePendendo.toString());
		HttpEntityProvider provider = new HttpEntityProvider() {

			public AbstractHttpEntity provideEntity() {
				try {
					return new StringEntity(incidentePendendo.toJSONObject()
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

		new HttpPostRequester(provider)
				.asyncPost("http://uspservices.deusanyjunior.dj/incidente", this);
	}

	@Override
	public void onStart() {
		super.onStart();
		checkIncidents();
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(Logado.this, Registrar.class);
		i.putExtra("username", userName);
		i.putExtra("uspid", uspID);
		startActivity(i);
	}

	@Override
	public void onHttpResponse(JSONObject response) {
		if (response == null) {
			Toast.makeText(
					Logado.this,
					"Falha ao enviar. Uma nova tentativa será realizada quando possível.",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(Logado.this, "Pendência enviada", Toast.LENGTH_LONG)
					.show();
			Log.d("Resposta do Incidente", response.toString());
			incidentePendendo.cleanCache(db);
		}
		incidentePendendo = null;
		checkIncidents();
	}

}
