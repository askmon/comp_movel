package usp.ime.movel.ouvidoria;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.web.HttpEntityProvider;
import usp.ime.movel.ouvidoria.web.HttpPostRequest;
import usp.ime.movel.ouvidoria.web.InsecureHttpClientFactory;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener,
		OnHttpResponseListener {

	private EditText user, pass;
	private Button mSubmit;

	private static final String LOGIN_URL = "https://social.stoa.usp.br/plugin/stoa/authenticate/";

	// JSON element ids from repsonse of php script:
	private static final String TAG_SUCCESS = "ok";
	// private static final String TAG_NUSP = "nusp";
	private static final String TAG_USERNAME = "username";
	// private static final String TAG_EMAIL = "email";
	private static final String TAG_ERROR = "error";
	private String name_user;
	private String uspid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// setup input fields
		user = (EditText) findViewById(R.id.username);
		pass = (EditText) findViewById(R.id.password);

		// setup buttons
		mSubmit = (Button) findViewById(R.id.login);

		// register listeners
		mSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login:
			HttpEntityProvider provider = new HttpEntityProvider() {
				public AbstractHttpEntity provideEntity() {
					try {
						return new UrlEncodedFormEntity(makePostParams());
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					return null;
				}
			};
			new HttpPostRequest(provider, new InsecureHttpClientFactory(), this)
					.execute(LOGIN_URL);
			break;

		default:
			break;
		}
	}

	@Override
	public void onHttpResponse(JSONObject response) {
		boolean success;
		String message;
		if (response == null) {
			message = "Sem resposta do servidor";
			return;
		} else
			try {
				Log.d("Login attempt", response.toString());
				// json success tag
				success = response.getBoolean(TAG_SUCCESS);
				if (success) {
					Log.d("Login Successful!", response.toString());
					message = "Entrou como " + response.getString(TAG_USERNAME);
					name_user = response.getString(TAG_USERNAME);
				} else {
					Log.d("Login Failure!", response.getString(TAG_ERROR));
					message = response.getString(TAG_ERROR);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return;
			}
		Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
		if (success) {
			Intent i = new Intent(Login.this, Logado.class);
			i.putExtra("username", name_user);
			i.putExtra("uspid", uspid);
			finish();
			startActivity(i);
		}
	}

	private ArrayList<NameValuePair> makePostParams() {
		uspid = user.getText().toString();
		String password = pass.getText().toString();
		// Building Parameters
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usp_id", uspid));
		params.add(new BasicNameValuePair("password", password));
		return params;
	}

}
