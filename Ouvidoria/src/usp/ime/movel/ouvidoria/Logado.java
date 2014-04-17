package usp.ime.movel.ouvidoria;

import usp.ime.movel.ouvidoria.device.SQLiteHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Logado extends OuvidoriaActivity implements OnClickListener {

	private Intent intent;
	private String userName;
	private String uspID;
	private Button mRegister;
	private SQLiteHelper db;

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
		db = new SQLiteHelper(this);
	}

	private void checkIncidents() {
		db.getAllIncidents();
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
		// finish();
		startActivity(i);
	}

}
