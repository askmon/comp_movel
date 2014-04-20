package usp.ime.movel.ouvidoria.web.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import usp.ime.movel.ouvidoria.web.HttpClientFactory;
import usp.ime.movel.ouvidoria.web.HttpGetRequester;
import android.test.InstrumentationTestCase;
import android.test.UiThreadTest;

public class HttpGetRequesterTest extends InstrumentationTestCase {

	private HttpGetRequester requester;
	private HttpClient client;
	private HttpClientFactory factory;
	
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		this.client = mock(HttpClient.class);
		this.factory = mock(HttpClientFactory.class);
		when(factory.makeHttpClient()).thenReturn(client);
		
		requester = new HttpGetRequester(factory);
	}
	
	@UiThreadTest
	public void testGet() throws InterruptedException, JSONException,
			ClientProtocolException, IOException {
		String expectedResponse = "{}"; // TODO usar uma resposta mais realista?
		JSONObject expectedJSON = new JSONObject(expectedResponse);
		HttpResponse httpResponse = mock(HttpResponse.class);
		HttpEntity responseEntity = mock(HttpEntity.class);

		when(httpResponse.getEntity()).thenReturn(responseEntity);
		when(client.execute(isA(HttpGet.class))).thenReturn(httpResponse);
		when(responseEntity.getContent()).thenReturn(
				new ByteArrayInputStream(expectedResponse.getBytes()));
		
		JSONObject response = requester.get("http://uspservices.deusanyjunior.dj/incidente/1.json");
		assertEquals(expectedJSON.toString(), response.toString());
	}
}
