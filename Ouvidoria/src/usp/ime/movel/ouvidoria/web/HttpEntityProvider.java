package usp.ime.movel.ouvidoria.web;

import org.apache.http.entity.AbstractHttpEntity;

public interface HttpEntityProvider {

	public AbstractHttpEntity provideEntity();

	public boolean hasContentType();

	public String getContentType();

}
