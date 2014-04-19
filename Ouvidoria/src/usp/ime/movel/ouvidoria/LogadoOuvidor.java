package usp.ime.movel.ouvidoria;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.AbstractHttpEntity;

import usp.ime.movel.ouvidoria.web.HttpEntityProvider;
import usp.ime.movel.ouvidoria.web.HttpPostRequest;
import usp.ime.movel.ouvidoria.web.InsecureHttpClientFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LogadoOuvidor extends OuvidoriaActivity implements OnClickListener {

	private Intent intent;
	private String username;
	private String uspid;
	private Button mListnew;
	private Button mShowmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logado_ouvidor);
		intent = getIntent();
		username = intent.getStringExtra("username");
		uspid = intent.getStringExtra("uspid");
		TextView user = (TextView) findViewById(R.id.textView1);
		user.setText("Usuário: " + username);
		mListnew = (Button) findViewById(R.id.listnew);
		mListnew.setOnClickListener(this);
		mListnew = (Button) findViewById(R.id.showmap);
		mListnew.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!getConnectionState().isConnected())
			Toast.makeText(LogadoOuvidor.this, "Sem conexão", Toast.LENGTH_LONG)
					.show();
		else if (getBatteryState().getLevel() <= 15
				&& getConnectionState().getType() != "WIFI") {
			Toast.makeText(LogadoOuvidor.this, "Pouca bateria e sem WIFI",
					Toast.LENGTH_LONG).show();
		} else {
			Intent i;
			switch (v.getId()) {
			case R.id.listnew:
				i = new Intent(LogadoOuvidor.this, ListarNovos.class);
				i.putExtra("username", username);
				i.putExtra("uspid", uspid);
				startActivity(i);
				break;
			case R.id.showmap:
				i = new Intent(LogadoOuvidor.this, Mapa.class);
				startActivity(i);
				break;
			default:
				break;
			}
		}
	}

}
