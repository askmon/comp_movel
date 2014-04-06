package usp.ime.movel.ouvidoria;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class Registrar extends Activity {

	private Intent intent;
	private String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registrar);
		intent = getIntent();
		username = intent.getStringExtra("username");
		TextView user = (TextView)findViewById(R.id.textView1);
		user.setText("Usuário: " + username);
	}

}
