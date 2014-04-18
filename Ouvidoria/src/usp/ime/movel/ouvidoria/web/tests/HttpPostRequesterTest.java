package usp.ime.movel.ouvidoria.web.tests;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.Login;
import usp.ime.movel.ouvidoria.web.HttpEntityProvider;
import usp.ime.movel.ouvidoria.web.HttpPostRequester;
import usp.ime.movel.ouvidoria.web.InsecureHttpClientFactory;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;
import android.test.UiThreadTest;

public class HttpPostRequesterTest extends TestCase implements OnHttpResponseListener {

	HttpPostRequester requester;
	HttpEntityProvider provider;
	Login login;
	CountDownLatch signal;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		login = new Login();
		final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usp_id", "10001"));
		params.add(new BasicNameValuePair("password", "claropassobluestrawmeuthorium"));
		signal = new CountDownLatch(1);
		provider = new HttpEntityProvider() {
			public AbstractHttpEntity provideEntity() {
				try {
					return new UrlEncodedFormEntity(params);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		requester = new HttpPostRequester(login, new InsecureHttpClientFactory(), provider);
	}
	
	@UiThreadTest
	public void testPost() throws InterruptedException, JSONException {
		String response = "{\"ok\":true,\"nusp\":\"10001\",\"username\":\"teste\",\"email\":\"teste@example.com\"}";
		JSONObject jsonObject = new JSONObject(response);

		requester.post("https://social.stoa.usp.br/plugin/stoa/authenticate/");
		signal.await(30, TimeUnit.SECONDS);

		assertTrue(login.getCheck());
		assertEquals(login.getResponseMessage(), jsonObject.toString());
	}
	
	@Override
	public void onHttpResponse(JSONObject response) {
		signal.countDown();
	}

}
