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

	private HttpClientFactory clientFactory;

	public HttpGetRequester(HttpClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	public HttpGetRequester() {
		this(new DefaultHttpClientFactory());
	}

	public JSONObject get(String url) {
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

	public void asyncGet(String url, OnHttpResponseListener httpResponseListener) {
		new HttpGetRequest(httpResponseListener).execute(url);
	}

	private class HttpGetRequest extends AsyncTask<String, String, JSONObject> {

		private OnHttpResponseListener responseListener;

		public HttpGetRequest(OnHttpResponseListener responseListener) {
			this.responseListener = responseListener;
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			String url = params[0];
			return HttpGetRequester.this.get(url);
		}

		protected void onPostExecute(JSONObject result) {
			responseListener.onHttpResponse(result);
		}
	}
}
