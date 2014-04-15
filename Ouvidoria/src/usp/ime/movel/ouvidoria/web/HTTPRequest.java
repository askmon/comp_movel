package usp.ime.movel.ouvidoria.web;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class HTTPRequest extends AsyncTask<String, String, HttpResponse> {

	private IHttpEntityProvider entityProvider;
	private IHTTPClientFactory clientFactory;

	public HTTPRequest(IHttpEntityProvider entityProvider,
			IHTTPClientFactory clientFactory) {
		this.entityProvider = entityProvider;
		this.clientFactory = clientFactory;
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
		JSONparser parser;
		try {
			parser = new JSONparser(resultMessage.getEntity().getContent());
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			// .show();
			return;
		}
		// Toast.makeText(Registrar.this, "Enviado", Toast.LENGTH_LONG).show();
		Log.d("Resutado", parser.parse().toString());
	}
}
