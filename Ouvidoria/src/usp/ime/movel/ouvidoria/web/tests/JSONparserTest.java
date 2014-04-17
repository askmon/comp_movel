package usp.ime.movel.ouvidoria.web.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.junit.Test;

import usp.ime.movel.ouvidoria.web.JSONParser;

public class JSONparserTest {

	private JSONObject jsonObject;
	private String string = "{\"incidentrecord\":{\"photo\":\"b64\",\"created_at\":\"2014-04-16T13:20:29-03:00\"," +
			"\"latitude\":46.0,\"updated_at\":\"2014-04-16T13:20:29-03:00\",\"id\":61," +
			"\"localization\":\"ime\",\"description\":\"alone in the dark 6\",\"longitude\":-23.0," +
			"\"login\":\"\",\"status\":\"Aberto\",\"user\":8883723}}";
	
	@Test(expected = NullPointerException.class)
	public void testExceptionWhenInputStreamIsNull () {
		JSONParser jsonParser = new JSONParser(null);
		jsonParser.parse();
	}
	
	@Test
	public void testParseOfInputStremIntoAnJSONObject () throws UnsupportedEncodingException, Exception {
		JSONObject obj = new JSONObject(string);
		JSONParser jsonParser = new JSONParser(new ByteArrayInputStream(string.getBytes()));
		jsonObject = jsonParser.parse();
		assertNotNull(jsonObject);
		assertEquals(obj.toString(), jsonObject.toString());
	}
}
