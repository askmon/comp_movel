package usp.ime.movel.ouvidoria.web;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import android.os.AsyncTask;

public class HttpPostRequester {

	private HttpClientFactory clientFactory;
	private HttpEntityProvider entityProvider;

	public HttpPostRequester(HttpEntityProvider entityProvider) {
		this(new DefaultHttpClientFactory(), entityProvider);
	}

	public HttpPostRequester(HttpClientFactory clientFactory,
			HttpEntityProvider entityProvider) {
		this.entityProvider = entityProvider;
		this.clientFactory = clientFactory;
	}

	public JSONObject post(String url) {
		HttpClient httpClient = clientFactory.makeHttpClient();
		HttpPost post = new HttpPost(url);
		post.setEntity(entityProvider.provideEntity());
		if (entityProvider.hasContentType()) {
			String contentType = entityProvider.getContentType();
			post.setHeader("Accept", contentType);
			post.setHeader("Content-type", contentType);
		}
		try {
			HttpResponse response = httpClient.execute(post);
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

	public void asyncPost(String url,
			OnHttpResponseListener httpResponseListener) {
		new HttpPostRequest(httpResponseListener).execute(url);
	}

	private class HttpPostRequest extends AsyncTask<String, String, JSONObject> {

		private OnHttpResponseListener responseListener;

		public HttpPostRequest(OnHttpResponseListener responseListener) {
			this.responseListener = responseListener;
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			String url = params[0];
			return HttpPostRequester.this.post(url);
		}

		protected void onPostExecute(JSONObject result) {
			responseListener.onHttpResponse(result);
		}
	}
}
