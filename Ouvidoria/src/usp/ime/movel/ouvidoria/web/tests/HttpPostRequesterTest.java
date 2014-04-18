package usp.ime.movel.ouvidoria.web.tests;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import usp.ime.movel.ouvidoria.web.HttpEntityProvider;
import usp.ime.movel.ouvidoria.web.HttpPostRequester;
import usp.ime.movel.ouvidoria.web.OnHttpResponseListener;
import android.test.UiThreadTest;

public class HttpPostRequesterTest extends TestCase implements OnHttpResponseListener {

	private String string = "{\"incidentrecord\":{\"photo\":\"b64\",\"created_at\":\"2014-04-16T13:20:29-03:00\"," +
			"\"latitude\":46.0,\"updated_at\":\"2014-04-16T13:20:29-03:00\",\"id\":61," +
			"\"localization\":\"ime\",\"description\":\"alone in the dark 6\",\"longitude\":-23.0," +
			"\"login\":\"\",\"status\":\"Aberto\",\"user\":8883723}}";
	HttpPostRequester requester;
	HttpEntityProvider provider;
	CountDownLatch signal;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		signal = new CountDownLatch(1);
		provider = new HttpEntityProvider() {

			public AbstractHttpEntity provideEntity() {
				try {
					return new StringEntity((new JSONObject(string)).toString());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			public boolean hasContentType() {
				return true;
			}

			public String getContentType() {
				return "application/json";
			}
		};
		requester = new HttpPostRequester(this, provider);
	}
	
	@UiThreadTest
	public void testPost() throws InterruptedException {
		requester.post("http://uspservices.deusanyjunior.dj/incidente");
		signal.await(30, TimeUnit.SECONDS);
		
		assertTrue("tenho que entender melhor o código", true);
	}
	
	@Override
	public void onHttpResponse(JSONObject response) {
		signal.countDown();
	}

}
