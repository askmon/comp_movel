package usp.ime.movel.ouvidoria;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

	private EditText user, pass;
	private Button mSubmit;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONparser jsonParser = new JSONparser();

	// php login script location:

	// localhost :
	// testing on your device
	// put your local ip instead, on windows, run CMD > ipconfig
	// or in mac's terminal type ifconfig and look for the ip under en0 or en1
	// private static final String LOGIN_URL =
	// "http://xxx.xxx.x.x:1234/webservice/login.php";

	// testing on Emulator:
	private static final String LOGIN_URL = "https://social.stoa.usp.br/plugin/stoa/authenticate/";

	// testing from a real server:
	// private static final String LOGIN_URL =
	// "http://www.yourdomain.com/webservice/login.php";

	// JSON element ids from repsonse of php script:
	private static final String TAG_SUCCESS = "ok";
	private static final String TAG_NUSP = "nusp";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_ERROR = "error";
	private String name_user;
	private String uspid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
			new AttemptLogin().execute();
			break;

		default:
			break;
		}
	}

	class AttemptLogin extends AsyncTask<String, String, JSONObject> {
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this.getApplicationContext());
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			// pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			// TODO Auto-generated method stub
			String username = user.getText().toString();
			uspid = username;
			String password = pass.getText().toString();
			// Building Parameters
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("usp_id", username));
			params.add(new BasicNameValuePair("password", password));

			Log.d("request!", "starting");
			// getting product details by making HTTP request
			return jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(JSONObject resultMessage) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (resultMessage == null) {
				return;
			}

			boolean success;
			String message;

			try {
				Log.d("Login attempt", resultMessage.toString());

				// json success tag
				success = resultMessage.getBoolean(TAG_SUCCESS);

				if (success) {
					Log.d("Login Successful!", resultMessage.toString());
					message = "Entrou como "
							+ resultMessage.getString(TAG_USERNAME);
					name_user = resultMessage.getString(TAG_USERNAME);
				} else {
					Log.d("Login Failure!", resultMessage.getString(TAG_ERROR));
					message = resultMessage.getString(TAG_ERROR);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return;
			}

			Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();

			if (success) {
				Intent i = new Intent(Login.this, Logado.class);
				i.putExtra("username", name_user);
				i.putExtra("usp_id", uspid);
				finish();
				startActivity(i);
			}

		}

	}

}
