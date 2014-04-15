package usp.ime.movel.ouvidoria.web;

import org.apache.http.entity.AbstractHttpEntity;

public abstract class HttpEntityProvider {

	public abstract AbstractHttpEntity provideEntity();

	public boolean hasContentType() {
		return false;
	}

	public String getContentType() {
		return null;
	}

}
