package usp.ime.movel.ouvidoria.tests;

import usp.ime.movel.ouvidoria.ListarNovos;
import usp.ime.movel.ouvidoria.LogadoOuvidor;
import usp.ime.movel.ouvidoria.Mapa;
import usp.ime.movel.ouvidoria.R;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.TextView;

public class LogadoOuvidorTest extends ActivityInstrumentationTestCase2<LogadoOuvidor> {

	private LogadoOuvidor ouvidorActivity;
	private Button listButton;
	private Button mapButton;
	private TextView usernameTextView;
	private Intent intent;
	
	public LogadoOuvidorTest() {
		super(LogadoOuvidor.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		intent = new Intent();
		intent.putExtra("username", "teste");
		setActivityIntent(intent);
		ouvidorActivity = getActivity();
		listButton = (Button) ouvidorActivity.findViewById(R.id.listnew);
		mapButton = (Button) ouvidorActivity.findViewById(R.id.showmap);
		usernameTextView = (TextView) ouvidorActivity.findViewById(R.id.textView1);
	}
	
	public void testPreconditions() {
	    assertNotNull("ouvidorActivity is null", ouvidorActivity);
	    assertNotNull("listButton is null", listButton);
	    assertNotNull("mapButton is null", mapButton);
	    assertNotNull("usernameTextView is null", usernameTextView);
	}
	
	public void testUsernameTextView_labelText() {
		final String expected = "Usuário: teste";
		final String actual = usernameTextView.getText().toString();
		assertEquals(expected, actual);
	}
	
	@MediumTest
	public void testSendMessageToListarNovosActivity() {
		// Set up an ActivityMonitor
		ActivityMonitor listarNovosMonitor =
		        getInstrumentation().addMonitor(ListarNovos.class.getName(),
		        null, false);

		// Validate that ReceiverActivity is started
		TouchUtils.clickView(this, listButton);
		ListarNovos listarNovosActivity = (ListarNovos)
		        listarNovosMonitor.waitForActivityWithTimeout(1000);
		assertNotNull("logadoActivity is null", listarNovosActivity);
		assertEquals("Monitor for logadoActivity has not been called",1, listarNovosMonitor.getHits());
		assertEquals("Activity is of wrong type", ListarNovos.class, listarNovosActivity.getClass());

		// Finish the Registrar activity and remove the ActivityMonitor
		listarNovosActivity.finish();
		getInstrumentation().removeMonitor(listarNovosMonitor);
	}
	
	@MediumTest
	public void testSendMessageToMapaActivity() {
		// Set up an ActivityMonitor
		ActivityMonitor mapaMonitor =
		        getInstrumentation().addMonitor(Mapa.class.getName(),
		        null, false);

		// Validate that ReceiverActivity is started
		TouchUtils.clickView(this, mapButton);
		Mapa mapaActivity = (Mapa)
		        mapaMonitor.waitForActivityWithTimeout(1000);
		assertNotNull("logadoActivity is null", mapaActivity);
		assertEquals("Monitor for logadoActivity has not been called",1, mapaMonitor.getHits());
		assertEquals("Activity is of wrong type", Mapa.class, mapaActivity.getClass());

		// Finish the Registrar activity and remove the ActivityMonitor
		mapaActivity.finish();
		getInstrumentation().removeMonitor(mapaMonitor);
	}
}
