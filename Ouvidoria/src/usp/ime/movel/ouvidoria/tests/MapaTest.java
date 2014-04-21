package usp.ime.movel.ouvidoria.tests;

import static org.mockito.Mockito.*;

import com.google.android.gms.maps.GoogleMap;

import usp.ime.movel.ouvidoria.IncidenteUpdater;
import usp.ime.movel.ouvidoria.Mapa;
import android.test.ActivityInstrumentationTestCase2;

public class MapaTest extends ActivityInstrumentationTestCase2<Mapa> {

	private Mapa mapaActivity;
	
	public MapaTest() {
		super(Mapa.class);
	}

	public void setUp() {
		mapaActivity = getActivity();
	}
	
	public void testAnything() {
		assertTrue(true);
	}
	
}
