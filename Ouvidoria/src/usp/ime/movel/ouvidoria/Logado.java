package usp.ime.movel.ouvidoria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Logado extends Activity implements OnClickListener {

	private Intent intent;
	private String username;
	private Button mRegister;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logado);
		intent = getIntent();
		username = intent.getStringExtra("username");
		TextView user = (TextView)findViewById(R.id.textView1);
		user.setText("Usuário: " + username);
		mRegister = (Button) findViewById(R.id.register);
		mRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(Logado.this, Registrar.class);
		i.putExtra("username", username);
		finish();
		startActivity(i);
	}

}
