package usp.ime.movel.ouvidoria.web;

import org.apache.http.HttpEntity;

public abstract class HttpEntityProvider {

	public abstract HttpEntity provideEntity();

	public boolean hasContentType() {
		return false;
	}

	public String getContentType() {
		return null;
	}

}
