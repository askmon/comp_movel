package usp.ime.movel.ouvidoria.web;

import org.apache.http.impl.client.DefaultHttpClient;

public interface HttpClientFactory {

	public DefaultHttpClient makeHttpClient();

}
