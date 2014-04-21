package usp.ime.movel.ouvidoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BuscaLocal extends OuvidoriaActivity implements OnClickListener {

	private EditText local;
	private Button mSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buscalocal);

		// setup input fields
		local = (EditText) findViewById(R.id.local);
		
		// setup buttons
		mSubmit = (Button) findViewById(R.id.submitlocal);

		// register listeners
		mSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.submitlocal:
			i = new Intent(BuscaLocal.this, ListarLocal.class);
			String localidade = local.getText().toString();
			i.putExtra("local", localidade);
			startActivity(i);
			break;

		default:
			break;
		}
	}

}
