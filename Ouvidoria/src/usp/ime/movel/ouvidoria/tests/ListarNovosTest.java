package usp.ime.movel.ouvidoria.tests;

import static org.mockito.Mockito.*;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit3.PowerMockSuite;

import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestSuite;
import usp.ime.movel.ouvidoria.IncidenteUpdater;
import usp.ime.movel.ouvidoria.ListarNovos;
import usp.ime.movel.ouvidoria.R;
import usp.ime.movel.ouvidoria.device.SQLiteHelper;
import usp.ime.movel.ouvidoria.model.Incidente;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;
import android.widget.TextView;

@PrepareForTest(ListarNovos.class)
public class ListarNovosTest extends
		ActivityInstrumentationTestCase2<ListarNovos> {

	private ListarNovos listarNovosActivity;
	private TextView[] incidentTexts;
	private ImageView[] incidentImages;
	private IncidenteUpdater updater;
	private Incidente incidente;
	private SQLiteHelper db;
	private List<Incidente> incidentList;

	public ListarNovosTest() {
		super(ListarNovos.class);
	}

	@SuppressWarnings("unchecked")
	public static TestSuite suite() throws Exception {
		return new PowerMockSuite(ListarNovosTest.class);
	}

	public void setUp() throws Exception {
		updater = mock(IncidenteUpdater.class);
		// db = mock(SQLiteHelper.class);

		incidente = mock(Incidente.class);
		incidentList = Arrays.asList(incidente, incidente, incidente,
				incidente, incidente);

		// whenNew(SQLiteHelper.class).withArguments(listarNovosActivity).thenReturn(db);
		whenNew(IncidenteUpdater.class).withArguments(any(ListarNovos.class),
				any(ListarNovos.class)).thenReturn(updater);
		// when(db.getAllIncidents(Incidente.STORED_INCIDENT_TABLE)).thenReturn(incidentList);
		when(updater.getIncidentes()).thenReturn(incidentList);
		when(incidente.getDescription()).thenReturn("descricao");
		when(incidente.getLocalization()).thenReturn("casa");
		when(incidente.getStatus()).thenReturn("Oculto");

		listarNovosActivity = getActivity();
		incidentTexts = new TextView[5];
		incidentImages = new ImageView[5];
		incidentTexts[0] = (TextView) listarNovosActivity
				.findViewById(R.id.ocorrencia1);
		incidentTexts[1] = (TextView) listarNovosActivity
				.findViewById(R.id.ocorrencia2);
		incidentTexts[2] = (TextView) listarNovosActivity
				.findViewById(R.id.ocorrencia3);
		incidentTexts[3] = (TextView) listarNovosActivity
				.findViewById(R.id.ocorrencia4);
		incidentTexts[4] = (TextView) listarNovosActivity
				.findViewById(R.id.ocorrencia5);
		incidentImages[0] = (ImageView) listarNovosActivity
				.findViewById(R.id.ivImage1);
		incidentImages[1] = (ImageView) listarNovosActivity
				.findViewById(R.id.ivImage2);
		incidentImages[2] = (ImageView) listarNovosActivity
				.findViewById(R.id.ivImage3);
		incidentImages[3] = (ImageView) listarNovosActivity
				.findViewById(R.id.ivImage4);
		incidentImages[4] = (ImageView) listarNovosActivity
				.findViewById(R.id.ivImage5);
	}

	public void testPreconditions() {
		assertNotNull("listarNovosActivity is null", listarNovosActivity);
		assertNotNull("incidentTexts[0] is null", incidentTexts[0]);
		assertNotNull("incidentTexts[1] is null", incidentTexts[1]);
		assertNotNull("incidentTexts[2] is null", incidentTexts[2]);
		assertNotNull("incidentTexts[3] is null", incidentTexts[3]);
		assertNotNull("incidentTexts[4] is null", incidentTexts[4]);
		assertNotNull("incidentImages[0] is null", incidentImages[0]);
		assertNotNull("incidentImages[1] is null", incidentImages[1]);
		assertNotNull("incidentImages[2] is null", incidentImages[2]);
		assertNotNull("incidentImages[3] is null", incidentImages[3]);
		assertNotNull("incidentImages[4] is null", incidentImages[4]);
	}

	public void testIncidentTextView_labelText() {
		final String expected = "----------\n"
				+ "Descrição: descricao\nLocal: casa\nStatus: Oculto\n";
		final String actual = incidentTexts[0].getText().toString();
		assertEquals(expected, actual);
	}
}
