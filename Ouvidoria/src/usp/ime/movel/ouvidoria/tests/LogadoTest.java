package usp.ime.movel.ouvidoria.tests;

import usp.ime.movel.ouvidoria.Logado;
import usp.ime.movel.ouvidoria.R;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

public class LogadoTest extends ActivityInstrumentationTestCase2<Logado>{

	private Logado logadoActivity;
	private Button registerButton;
	
	public LogadoTest() {
		super(Logado.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		logadoActivity = getActivity();
		registerButton = (Button) logadoActivity.findViewById(R.id.register);
	}
	
	public void testPreconditions() {
	    assertNotNull("logadoActivity is null", logadoActivity);
	    assertNotNull("registerButton is null", registerButton);
	}

}
