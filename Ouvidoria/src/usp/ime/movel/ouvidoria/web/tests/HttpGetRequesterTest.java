package usp.ime.movel.ouvidoria.web.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.ListarNovos;
import usp.ime.movel.ouvidoria.web.HttpGetRequester;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;
import android.test.UiThreadTest;

public class HttpGetRequesterTest extends TestCase implements OnHttpResponseListener {

	HttpGetRequester requester;
	ListarNovos listarNovos;
	CountDownLatch signal;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		listarNovos = new ListarNovos();
		signal = new CountDownLatch(1);
		requester = new HttpGetRequester(listarNovos);
	}
	
	@UiThreadTest
	public void testGet() throws InterruptedException, JSONException {
		requester.get("http://uspservices.deusanyjunior.dj/incidente/1.json");
		signal.await(30, TimeUnit.SECONDS);

		assertTrue(listarNovos.getCheck());
	}
	
	@Override
	public void onHttpResponse(JSONObject response) {
		signal.countDown();
	}

}
