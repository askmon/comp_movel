package usp.ime.movel.ouvidoria.web.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import static org.mockito.Mockito.*;

import usp.ime.movel.ouvidoria.web.HttpClientFactory;
import usp.ime.movel.ouvidoria.web.HttpEntityProvider;
import usp.ime.movel.ouvidoria.web.HttpPostRequester;
import android.test.InstrumentationTestCase;
import android.test.UiThreadTest;

public class HttpPostRequesterTest extends InstrumentationTestCase {

	// Mocks
	private HttpPostRequester requester;
	private HttpEntityProvider provider;
	private HttpEntity requestEntity;
	private HttpClientFactory factory;
	private HttpClient client;

	// Actual objects
	private String expectedResponse;

	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		provider = mock(HttpEntityProvider.class);
		requestEntity = mock(HttpEntity.class);
		when(provider.provideEntity()).thenReturn(requestEntity);

		factory = mock(HttpClientFactory.class);
		client = mock(HttpClient.class);
		when(factory.makeHttpClient()).thenReturn(client);

		requester = new HttpPostRequester(factory, provider);
	}

	@UiThreadTest
	public void testPost() throws InterruptedException, JSONException,
			ClientProtocolException, IOException {

		expectedResponse = "{\"ok\":true,\"nusp\":\"10001\",\"username\":\"teste\",\"email\":\"teste@example.com\"}";
		JSONObject expectedJSON = new JSONObject(expectedResponse);
		HttpResponse httpResponse = mock(HttpResponse.class);
		HttpEntity responseEntity = mock(HttpEntity.class);

		when(httpResponse.getEntity()).thenReturn(responseEntity);
		when(client.execute(isA(HttpPost.class))).thenReturn(httpResponse);
		when(responseEntity.getContent()).thenReturn(
				new ByteArrayInputStream(expectedResponse.getBytes()));

		JSONObject response = requester.post("https://social.stoa.usp.br/plugin/stoa/authenticate/");
		assertEquals(expectedJSON.toString(), response.toString());
		verify(provider).provideEntity();
	}
}
