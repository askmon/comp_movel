package usp.ime.movel.ouvidoria.web;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import android.os.AsyncTask;


public class HttpGetRequester {

	private OnHttpResponseListener httpResponseListener;
	
	public HttpGetRequester(OnHttpResponseListener httpResponseListener) {
		this.httpResponseListener = httpResponseListener;
	}
	
	public void get(String url) {
		new HttpGetRequest(httpResponseListener).execute(url);
	}
	
	public class HttpGetRequest extends AsyncTask<String, String, JSONObject> {

		private HttpClientFactory clientFactory;
		private OnHttpResponseListener responseListener;

		public HttpGetRequest(
				HttpClientFactory clientFactory,
				OnHttpResponseListener responseListener) {
			this.clientFactory = clientFactory;
			this.responseListener = responseListener;
		}

		public HttpGetRequest(OnHttpResponseListener responseListener) {
			this(new DefaultHttpClientFactory(), responseListener);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			String url = params[0];
			HttpClient httpClient = clientFactory.makeHttpClient();
			HttpGet get = new HttpGet(url);
			try {
				HttpResponse response = httpClient.execute(get);
				HttpEntity entity = response.getEntity();
				if (entity != null)
					return new JSONParser(entity.getContent()).parse();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(JSONObject result) {
			responseListener.onHttpResponse(result);
		}
	}
}
