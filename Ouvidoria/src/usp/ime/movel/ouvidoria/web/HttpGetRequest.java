package usp.ime.movel.ouvidoria.web;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;

public class HttpGetRequest extends AsyncTask<String, String, JSONObject> {

	private HttpEntityProvider entityProvider;
	private HttpClientFactory clientFactory;
	private OnHttpResponseListener responseListener;

	public HttpGetRequest(
			HttpClientFactory clientFactory,
			OnHttpResponseListener responseListener) {
		this.clientFactory = clientFactory;
		this.responseListener = responseListener;
	}

	public HttpGetRequest(HttpEntityProvider entityProvider,
			OnHttpResponseListener responseListener) {
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String url = params[0];
		DefaultHttpClient httpClient = clientFactory.makeHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null)
				return new JSONparser(entity.getContent()).parse();
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
