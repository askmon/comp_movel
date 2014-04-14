package usp.ime.movel.ouvidoria.tests;

import usp.ime.movel.ouvidoria.Logado;
import usp.ime.movel.ouvidoria.R;
import usp.ime.movel.ouvidoria.Registrar;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.TextView;

public class LogadoTest extends ActivityInstrumentationTestCase2<Logado>{

	private Logado logadoActivity;
	private Button registerButton;
	private TextView usernameTextView;
	private Intent intent;
	
	public LogadoTest() {
		super(Logado.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		intent = new Intent();
		intent.putExtra("username", "diegomartinez");
		setActivityIntent(intent);
		logadoActivity = getActivity();
		registerButton = (Button) logadoActivity.findViewById(R.id.register);
		usernameTextView = (TextView) logadoActivity.findViewById(R.id.textView1);
	}

	public void testPreconditions() {
	    assertNotNull("logadoActivity is null", logadoActivity);
	    assertNotNull("registerButton is null", registerButton);
	    assertNotNull("usernameTextView is null", usernameTextView);
	}

	public void testUsernameTextView_labelText() {
		final String expected = "Usuário: diegomartinez";
		final String actual = usernameTextView.getText().toString();
		assertEquals(expected, actual);
	}

	@MediumTest
	public void testSendMessageToLogadoActivity() {
		// Set up an ActivityMonitor
		ActivityMonitor registrarMonitor =
		        getInstrumentation().addMonitor(Registrar.class.getName(),
		        null, false);

		// Validate that ReceiverActivity is started
		TouchUtils.clickView(this, registerButton);
		Registrar registrarActivity = (Registrar)
		        registrarMonitor.waitForActivityWithTimeout(1000);
		assertNotNull("logadoActivity is null", registrarActivity);
		assertEquals("Monitor for logadoActivity has not been called",1, registrarMonitor.getHits());
		assertEquals("Activity is of wrong type", Registrar.class, registrarActivity.getClass());

		// Finish the Registrar activity and remove the ActivityMonitor
		registrarActivity.finish();
		getInstrumentation().removeMonitor(registrarMonitor);
	}
}
