package usp.ime.movel.ouvidoria;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.web.AbstractHttpEntityProvider;
import usp.ime.movel.ouvidoria.web.HttpEntityProvider;
import usp.ime.movel.ouvidoria.web.HttpPostRequester;
import usp.ime.movel.ouvidoria.web.InsecureHttpClientFactory;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends OuvidoriaActivity implements OnClickListener,
		OnHttpResponseListener {

	private EditText user, pass;
	private Button mSubmit;
	private Button mSubmitOuvidor;
	private boolean check = false;
	private String responseMessage;

	private static final String LOGIN_URL = "https://social.stoa.usp.br/plugin/stoa/authenticate/";

	// JSON element ids from repsonse of php script:
	private static final String TAG_SUCCESS = "ok";
	// private static final String TAG_NUSP = "nusp";
	private static final String TAG_USERNAME = "username";
	// private static final String TAG_EMAIL = "email";
	private static final String TAG_ERROR = "error";
	private String userName;
	private String uspID;
	private boolean ouvidor = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		// setup input fields
		user = (EditText) findViewById(R.id.username);
		pass = (EditText) findViewById(R.id.password);

		// setup buttons
		mSubmit = (Button) findViewById(R.id.login);
		mSubmitOuvidor = (Button) findViewById(R.id.login_ouvidor);

		// register listeners
		mSubmit.setOnClickListener(this);
		mSubmitOuvidor.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			if (!getConnectionState().isConnected())
				Toast.makeText(Login.this, "Sem conexão", Toast.LENGTH_LONG)
						.show();
			else if (getBatteryState().getLevel() <= 15
					&& getConnectionState().getType() != "WIFI") {
				Toast.makeText(Login.this, "Pouca bateria e sem WIFI",
						Toast.LENGTH_LONG).show();
			} else {
				HttpEntityProvider provider = new AbstractHttpEntityProvider() {
					public AbstractHttpEntity provideEntity() {
						try {
							return new UrlEncodedFormEntity(makePostParams());
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						return null;
					}
				};
				new HttpPostRequester(this, new InsecureHttpClientFactory(),
						provider).asyncPost(LOGIN_URL);
			}
			break;
		
		case R.id.login_ouvidor:
			ouvidor = true;
			HttpEntityProvider provider_ouvidor = new AbstractHttpEntityProvider() {
				public HttpEntity provideEntity() {
					try {
						return new UrlEncodedFormEntity(makePostParams());
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					return null;
				}
			};
			new HttpPostRequester(this, new InsecureHttpClientFactory(), provider_ouvidor)
					.asyncPost(LOGIN_URL);
			break;

		default:
			break;
		}
	}

	@Override
	public void onHttpResponse(JSONObject response) {
		check = true;
		boolean success;
		String message = "";
		if (response == null) {
			responseMessage = message = "Sem resposta do servidor";
			return;
		} else
			responseMessage = response.toString();
			try {
				Log.d("Login attempt", response.toString());
				// json success tag
				success = response.getBoolean(TAG_SUCCESS);
				if (success) {
					Log.d("Login Successful!", response.toString());
					message = "Entrou como " + response.getString(TAG_USERNAME);
					userName = response.getString(TAG_USERNAME);
				} else {
					Log.d("Login Failure!", response.getString(TAG_ERROR));
					message = response.getString(TAG_ERROR);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return;
			}
		try {
			Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
			if (success) {
				if(!ouvidor){
					Intent i = new Intent(Login.this, Logado.class);
					i.putExtra("username", userName);
					i.putExtra("uspid", uspID);
					finish();
					startActivity(i);
				}
				else{
					Intent i = new Intent(Login.this, LogadoOuvidor.class);
					i.putExtra("username", userName);
					i.putExtra("uspid", uspID);
					finish();
					startActivity(i);
				}
			}
		} catch (Exception e) {	}
	}

	private ArrayList<NameValuePair> makePostParams() {
		uspID = user.getText().toString();
		String password = pass.getText().toString();
		// Building Parameters
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usp_id", uspID));
		params.add(new BasicNameValuePair("password", password));
		return params;
	}

	public boolean getCheck() {
		return check;
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}
}
