package usp.ime.movel.ouvidoria.web;

import org.apache.http.HttpEntity;

public interface HttpEntityProvider {

	public HttpEntity provideEntity();

	public boolean hasContentType();

	public String getContentType();

}
