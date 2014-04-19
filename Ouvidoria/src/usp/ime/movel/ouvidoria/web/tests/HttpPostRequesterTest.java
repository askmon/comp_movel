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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import usp.ime.movel.ouvidoria.web.HttpClientFactory;
import usp.ime.movel.ouvidoria.web.HttpEntityProvider;
import usp.ime.movel.ouvidoria.web.HttpPostRequester;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;
import android.test.InstrumentationTestCase;
import android.test.UiThreadTest;

public class HttpPostRequesterTest extends InstrumentationTestCase {

	// Mocks
	private HttpPostRequester requester;
	private HttpEntityProvider provider;
	private HttpEntity requestEntity;
	private HttpClientFactory factory;
	private HttpClient client;
	private OnHttpResponseListener listener;

	// Actual objects
	private String expectedResponse;
	private CountDownLatch signal;

	protected void setUp() throws Exception {
		super.setUp();

		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		provider = mock(HttpEntityProvider.class);
		requestEntity = mock(HttpEntity.class);
		when(provider.provideEntity()).thenReturn(requestEntity);

		factory = mock(HttpClientFactory.class);
		client = mock(HttpClient.class);
		when(factory.makeHttpClient()).thenReturn(client);

		listener = mock(OnHttpResponseListener.class);

		signal = new CountDownLatch(1);

		requester = new HttpPostRequester(listener, factory, provider);
	}

	@UiThreadTest
	public void testPost() throws InterruptedException, JSONException,
			ClientProtocolException, IOException {

		expectedResponse = "{\"ok\":true,\"nusp\":\"10001\",\"username\":\"teste\",\"email\":\"teste@example.com\"}";
		HttpResponse response = mock(HttpResponse.class);
		HttpEntity responseEntity = mock(HttpEntity.class);

		when(response.getEntity()).thenReturn(responseEntity);
		when(client.execute(isA(HttpPost.class))).thenReturn(response);
		when(responseEntity.getContent()).thenReturn(
				new ByteArrayInputStream(expectedResponse.getBytes()));
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				JSONObject expectedJSON = new JSONObject(expectedResponse);
				JSONObject response = (JSONObject) invocation.getArguments()[0];
				assertEquals(expectedJSON.toString(), response.toString());
				signal.countDown();
				return null;
			}
		}).when(listener).onHttpResponse(any(JSONObject.class));

		requester.post("https://social.stoa.usp.br/plugin/stoa/authenticate/");
		//signal.await(30, TimeUnit.SECONDS);
		signal.await();

		assertEquals(0, signal.getCount());
		verify(provider).provideEntity();
	}
}
