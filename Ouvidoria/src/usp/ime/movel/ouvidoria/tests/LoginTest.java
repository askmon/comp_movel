package usp.ime.movel.ouvidoria.tests;

import usp.ime.movel.ouvidoria.Login;
import usp.ime.movel.ouvidoria.R;
import android.test.ActivityInstrumentationTestCase2;
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
}
