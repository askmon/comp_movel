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
	private Button mShowmap;
	private Button mListOpen;
	private Button mListSolving;
	private Button mListOculto;
	private Button mListSolved;

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
		mListOpen = (Button) findViewById(R.id.listopen);
		mListOpen.setOnClickListener(this);
		mListSolving = (Button) findViewById(R.id.listsolving);
		mListSolving.setOnClickListener(this);
		mListOculto = (Button) findViewById(R.id.listoculto);
		mListOculto.setOnClickListener(this);
		mListSolved = (Button) findViewById(R.id.listsolved);
		mListSolved.setOnClickListener(this);
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
			case R.id.listopen:
				System.out.println("Aqui");
				i = new Intent(LogadoOuvidor.this, ListarAbertos.class);
				i.putExtra("username", username);
				i.putExtra("uspid", uspid);
				startActivity(i);
				break;
			case R.id.listsolving:
				System.out.println("Aqui");
				i = new Intent(LogadoOuvidor.this, ListarAndamento.class);
				i.putExtra("username", username);
				i.putExtra("uspid", uspid);
				startActivity(i);
				break;
			case R.id.listoculto:
				System.out.println("Aqui");
				i = new Intent(LogadoOuvidor.this, ListarOcultos.class);
				i.putExtra("username", username);
				i.putExtra("uspid", uspid);
				startActivity(i);
				break;
			case R.id.listsolved:
				System.out.println("Aqui");
				i = new Intent(LogadoOuvidor.this, ListarResolvidos.class);
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
