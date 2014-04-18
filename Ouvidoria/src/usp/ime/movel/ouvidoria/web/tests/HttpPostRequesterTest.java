package usp.ime.movel.ouvidoria.web.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import static org.mockito.Mockito.*;

import usp.ime.movel.ouvidoria.web.HttpClientFactory;
import usp.ime.movel.ouvidoria.web.HttpEntityProvider;
import usp.ime.movel.ouvidoria.web.HttpPostRequester;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;
import android.test.UiThreadTest;

public class HttpPostRequesterTest extends TestCase implements
		OnHttpResponseListener {

	private HttpPostRequester requester;
	private HttpEntityProvider provider;
	// Login login;
	private CountDownLatch signal;
	private HttpEntity inputEntity;
	private String expectedResponse = "{\"ok\":true,\"nusp\":\"10001\",\"username\":\"teste\",\"email\":\"teste@example.com\"}";
	private HttpClientFactory factory;
	private HttpClient client;

	protected void setUp() throws Exception {
		super.setUp();

		inputEntity = mock(HttpEntity.class);
		factory = mock(HttpClientFactory.class);
		client = mock(HttpClient.class);
		when(factory.makeHttpClient()).thenReturn(client);
		signal = new CountDownLatch(1);
		provider = new HttpEntityProvider() {
			public HttpEntity provideEntity() {
				return inputEntity;
			}
		};
		requester = new HttpPostRequester(this, factory, provider);
	}

	@UiThreadTest
	public void testPost() throws InterruptedException, JSONException,
			ClientProtocolException, IOException {

		HttpResponse response = mock(HttpResponse.class);
		HttpEntity responseEntity = mock(AbstractHttpEntity.class);
		
		when(response.getEntity()).thenReturn(responseEntity);
		when(client.execute(isA(HttpPost.class))).thenReturn(response);
		when(responseEntity.getContent()).thenReturn(
				new ByteArrayInputStream(expectedResponse.getBytes()));
		
		requester.post("https://social.stoa.usp.br/plugin/stoa/authenticate/");
		signal.await(30, TimeUnit.SECONDS);

		assertEquals(0, signal.getCount());
		
	}

	@Override
	public void onHttpResponse(JSONObject response) {
		try {
			JSONObject jsonObject = new JSONObject(expectedResponse);
			assertEquals(jsonObject.toString(), response.toString());
			signal.countDown();
		} catch (JSONException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

}
