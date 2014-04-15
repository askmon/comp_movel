package usp.ime.movel.ouvidoria.web;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;

public class HttpRequest extends AsyncTask<String, String, HttpResponse> {

	private HttpEntityProvider entityProvider;
	private HttpClientFactory clientFactory;
	private OnHttpResponseListener responseListener;

	public HttpRequest(HttpEntityProvider entityProvider,
			HttpClientFactory clientFactory,
			OnHttpResponseListener responseListener) {
		this.entityProvider = entityProvider;
		this.clientFactory = clientFactory;
		this.responseListener = responseListener;
	}

	public HttpRequest(HttpEntityProvider entityProvider,
			OnHttpResponseListener responseListener) {
		this(entityProvider, new DefaultHttpClientFactory(), responseListener);
	}

	@Override
	protected HttpResponse doInBackground(String... params) {
		String url = params[0];
		DefaultHttpClient httpClient = clientFactory.makeHttpClient();
		HttpPost post = new HttpPost(url);
		post.setEntity(entityProvider.provideEntity());
		if (entityProvider.hasContentType()) {
			String contentType = entityProvider.getContentType();
			post.setHeader("Accept", contentType);
			post.setHeader("Content-type", contentType);
		}
		try {
			return httpClient.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(HttpResponse resultMessage) {
		JSONObject json = null;
		try {
			json = new JSONparser(resultMessage.getEntity().getContent()).parse();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		responseListener.onHttpResponse(json);
	}
}
