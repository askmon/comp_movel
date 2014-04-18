package usp.ime.movel.ouvidoria.web;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class DefaultHttpClientFactory implements HttpClientFactory {

	@Override
	public HttpClient makeHttpClient() {
		return new DefaultHttpClient();
	}

}
