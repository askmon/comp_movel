package usp.ime.movel.ouvidoria.web;

public abstract class AbstractHttpEntityProvider implements HttpEntityProvider {

	@Override
	public boolean hasContentType() {
		return false;
	}

	@Override
	public String getContentType() {
		return null;
	}

}
