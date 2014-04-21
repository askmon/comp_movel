package usp.ime.movel.ouvidoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BuscaDescricao extends OuvidoriaActivity implements OnClickListener {

	private EditText desc;
	private Button mSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buscadescricao);

		// setup input fields
		desc = (EditText) findViewById(R.id.desc);
		
		// setup buttons
		mSubmit = (Button) findViewById(R.id.submitdescricao);

		// register listeners
		mSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.submitdescricao:
			i = new Intent(BuscaDescricao.this, ListarDescricao.class);
			String descr = desc.getText().toString();
			i.putExtra("desc", descr);
			startActivity(i);
			break;

		default:
			break;
		}
	}

}
