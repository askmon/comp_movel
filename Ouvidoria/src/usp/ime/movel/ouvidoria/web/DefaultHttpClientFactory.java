package usp.ime.movel.ouvidoria.web;

import org.apache.http.impl.client.DefaultHttpClient;

public class DefaultHttpClientFactory implements HttpClientFactory {

	@Override
	public DefaultHttpClient makeHttpClient() {
		return new DefaultHttpClient();
	}

}
