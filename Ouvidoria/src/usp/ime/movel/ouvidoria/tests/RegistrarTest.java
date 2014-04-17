package usp.ime.movel.ouvidoria.tests;

import usp.ime.movel.ouvidoria.R;
import usp.ime.movel.ouvidoria.Registrar;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrarTest extends ActivityInstrumentationTestCase2<Registrar> {

	private Registrar registrarActivity;
	private TextView descriptionTextView, locationTextView, uspidTextView;
	private Button pictureButton;
	private EditText descriptionEditText, locationEditText;
	private Intent intent;
	
	public RegistrarTest() {
		super(Registrar.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		intent = new Intent();
		intent.putExtra("username", "teste");
		intent.putExtra("uspid", "10001");
		setActivityIntent(intent);
		registrarActivity = getActivity();
		pictureButton = (Button) registrarActivity.findViewById(R.id.picture);
		descriptionTextView = (TextView) registrarActivity.findViewById(R.id.textViewd);
		locationTextView = (TextView) registrarActivity.findViewById(R.id.textViewl);
		uspidTextView = (TextView) registrarActivity.findViewById(R.id.textView1);
		descriptionEditText = (EditText) registrarActivity.findViewById(R.id.description);
		locationEditText = (EditText) registrarActivity.findViewById(R.id.location);
	}
	
	public void testPreconditions() {
	    assertNotNull("registrarActivity is null", registrarActivity);
	    assertNotNull("descriptionTextView is null", descriptionTextView);
	    assertNotNull("locationTextView is null", locationTextView);
	    assertNotNull("uspidTextView is null", uspidTextView);
	    assertNotNull("pictureButton is null", pictureButton);
	    assertNotNull("locationEditText is null", locationEditText);
	    assertNotNull("descriptionEditText is null", descriptionEditText);
	}
	
	public void testLocationTextView_labelText() {
		final String expected = registrarActivity.getString(R.string.location_string);
		final String actual = locationTextView.getText().toString();
		assertEquals(expected, actual);
	}
	
	public void testUspidTextView_labelText() {
		final String expected = "Usuário: 10001";
		final String actual = uspidTextView.getText().toString();
		assertEquals(expected, actual);
	}
	
	public void testDescriptionTextView_labelText() {
		final String expected = registrarActivity.getString(R.string.description_string);
		final String actual = descriptionTextView.getText().toString();
		assertEquals(expected, actual);
	}
}
