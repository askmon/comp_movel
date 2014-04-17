package usp.ime.movel.ouvidoria.tests;

import usp.ime.movel.ouvidoria.Logado;
import usp.ime.movel.ouvidoria.Login;
import usp.ime.movel.ouvidoria.R;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginTest extends ActivityInstrumentationTestCase2<Login>{

	private Login loginActivity;
	private EditText userEditText, passwordEditText;
	private TextView usernameTextView, passwordTextView;
	private Button submitButton;
	
	public LoginTest() {
		super(Login.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		loginActivity = getActivity();
		userEditText = (EditText) loginActivity.findViewById(R.id.username);
		passwordEditText = (EditText) loginActivity.findViewById(R.id.password);
		submitButton = (Button) loginActivity.findViewById(R.id.login);
		usernameTextView = (TextView) loginActivity.findViewById(R.id.TextView01);
		passwordTextView = (TextView) loginActivity.findViewById(R.id.textView1);
	}
	
	public void testPreconditions() {
	    assertNotNull("loginActivity is null", loginActivity);
	    assertNotNull("userEditText is null", userEditText);
	    assertNotNull("passwordEditText is null", passwordEditText);
	    assertNotNull("submitButton is null", submitButton);
	}

	public void testUsernameTextView_labelText() {
		final String expected = loginActivity.getString(R.string.input_user);
		final String actual = usernameTextView.getText().toString();
		assertEquals(expected, actual);
	}

	public void testPasswordTextView_labelText() {
		final String expected = loginActivity.getString(R.string.input_pass);
		final String actual = passwordTextView.getText().toString();
		assertEquals(expected, actual);
	}

	@MediumTest
	public void testSendMessageToLogadoActivity() {
		// Set up an ActivityMonitor
		ActivityMonitor logadoMonitor =
		        getInstrumentation().addMonitor(Logado.class.getName(),
		        null, false);

		// Send string input value
		getInstrumentation().runOnMainSync(new Runnable() {
		    @Override
		    public void run() {
		        userEditText.requestFocus();
		    }
		});
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendStringSync("10001");
		getInstrumentation().waitForIdleSync();

		getInstrumentation().runOnMainSync(new Runnable() {
		    @Override
		    public void run() {
		        passwordEditText.requestFocus();
		    }
		});
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendStringSync("claropassobluestrawmeuthorium");
		getInstrumentation().waitForIdleSync();


		// Validate that ReceiverActivity is started
		TouchUtils.clickView(this, submitButton);
		Logado logadoActivity = (Logado)
		        logadoMonitor.waitForActivityWithTimeout(1000);
		assertNotNull("logadoActivity is null", logadoActivity);
		assertEquals("Monitor for logadoActivity has not been called",1, logadoMonitor.getHits());
		assertEquals("Activity is of wrong type", Logado.class, logadoActivity.getClass());

		// Finish the Logado activity and remove the ActivityMonitor
		logadoActivity.finish();
		getInstrumentation().removeMonitor(logadoMonitor);
	}
}
