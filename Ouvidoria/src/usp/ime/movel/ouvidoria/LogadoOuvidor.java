package usp.ime.movel.ouvidoria;

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
			Intent i = new Intent(LogadoOuvidor.this, ListarNovos.class);
			i.putExtra("username", username);
			i.putExtra("uspid", uspid);
			startActivity(i);
		}
	}

}