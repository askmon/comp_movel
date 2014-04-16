package usp.ime.movel.ouvidoria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LogadoOuvidor extends Activity implements OnClickListener {

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
		TextView user = (TextView)findViewById(R.id.textView1);
		user.setText("Usuário: " + username);
		mListnew = (Button) findViewById(R.id.listnew);
		mListnew.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(LogadoOuvidor.this, ListarNovos.class);
		i.putExtra("username", username);
		i.putExtra("uspid", uspid);
		//finish();
		startActivity(i);
	}
	
	
}
